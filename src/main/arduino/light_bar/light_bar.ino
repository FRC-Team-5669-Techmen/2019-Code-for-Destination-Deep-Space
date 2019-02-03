#include <Wire.h>

/* I2C address:
 *   0x11 - Sensor bar 1
 *   0x12 - Sensor bar 2
 * I2C register map:
 *   0x00 - Status register (unused, always 0x00)
 *   0x01 - Output register (the integer index of the sensor that is brightest.) (0xFF if no output was computed.)
 *   0x02 - Reserved.
 *   0x03 - "Serial number" - 0x01 for the first bar, 0x02 for the second.
 */

// Define 1 or 2 depending on which bar this is.
#define SENSOR_BAR_1

typedef unsigned int uint;

#ifdef SENSOR_BAR_1
const byte I2C_SLAVE_ADDRESS = 0x11;
const byte I2C_SERIAL_NUMBER = 0x01;
#else
const byte I2C_SLAVE_ADDRESS = 0x12;
const byte I2C_SERIAL_NUMBER = 0x02;
#endif

const byte I2C_DATA_SIZE = 4, NUM_SENSORS = 16;
const uint SAMPLES_PER_SENSOR = 8;
const uint OUTPUT_PRECISION = 8; // A value of 8 means 8 times as many output positions as there are sensors.
const int ANALOG_PIN = A0;
// Multiplexer pins must be attached as follows (for efficiency reasons):
// A = 10, B = 11, C = 12, D = 13
const uint CALIBRATION[NUM_SENSORS] = {
  0, 0, 0, 0, 0, 0, 0, 0,
  0, 0, 0, 0, 0, 0, 0, 0
};
byte i2cRegisters[4] = {0x00, 0xFF, 0x00, I2C_SERIAL_NUMBER};
byte *output = &i2cRegisters[1], *registerPointer = &i2cRegisters[0];

void setup() {
  Wire.begin(I2C_SLAVE_ADDRESS);
  Wire.onRequest(handleI2CRequest);
  Wire.onReceive(handleI2CReceive);

  // Sets the ADC to go 16x as fast without a decrease in accuracy.
  ADCSRA |= 0b00000100;
  ADCSRA &= 0b11111100;
  DDRB |= 0b00111100; // Set ports 10-13 as output ports.
}

void loop() {
  uint values[NUM_SENSORS];
  uint minValue = 65535; // Brightest value.
  uint maxValue = 0; // Darkest value.
  for (byte sensorIndex = 0; sensorIndex < NUM_SENSORS; sensorIndex++) {
    uint value = 0; // Because of how it is wired, lower voltages means brighter light
    // Write four-bit representation of sensor index to multiplexer.
    PORTB &= 0b11000011; // Clear ports 10-13.
    PORTB |= sensorIndex << 2; // Set ports 10-13 to the binary representation of the index.
    // Poll the analog pin multiple times to get several sensor readings.
    for (uint sample = 0; sample < SAMPLES_PER_SENSOR; sample++) {
      value += analogRead(ANALOG_PIN);
    }
    values[sensorIndex] = value;
    minValue = (value < minValue) ? value : minValue;
    maxValue = (value > maxValue) ? value : maxValue;
  }

  uint range, newMax;
  range = maxValue - minValue;
  newMax = minValue + range / 2; // Threshold to cut off mostly dark sensors.
  range = newMax - minValue;
  uint totalIndices = 0, totalWeight = 0;
  // Weighted average of all sensors based on their brightness relative to min and max values.
  // Sensors that are darker than newMax are not counted.
  for (uint sensorIndex = 0; sensorIndex < NUM_SENSORS; sensorIndex++) {
    if (values[sensorIndex] <= newMax) {
      uint weight = (newMax - values[sensorIndex]) / (range / OUTPUT_PRECISION);
      totalWeight += weight;
      totalIndices += sensorIndex * weight;
    }
  }
  
  *output = totalIndices / (totalWeight / OUTPUT_PRECISION);
}

void handleI2CRequest() {
  Wire.write(registerPointer, I2C_DATA_SIZE);
}

void handleI2CReceive(int bytesReceived) {
  if (bytesReceived == 0) return;
  registerPointer = i2cRegisters + Wire.read();
  if (registerPointer >= i2cRegisters + I2C_DATA_SIZE) {
    registerPointer = &i2cRegisters[0];
  }
  for (uint i = 0; i < bytesReceived - 1; i++) {
    // Get rid of extra data.
    Wire.read();
  }
}
