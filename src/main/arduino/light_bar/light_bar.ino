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
}

void loop() {
  byte brightestIndex = 0;
  uint brightestValue = 0;
  for (byte sensorIndex = 0; sensorIndex < NUM_SENSORS; sensorIndex++) {
    uint value = 0;
    for (uint sample = 0; sample < SAMPLES_PER_SENSOR; sample++) {
      value += analogRead(0);
    }
    if (value > brightestValue) {
      brightestValue = value;
      brightestIndex = sensorIndex;
    }
  }
  *output = brightestIndex;
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
