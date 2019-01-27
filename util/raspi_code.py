#!/usr/bin/env python3

from math import sqrt
import numpy
import cv2
from types import SimpleNamespace

def circle_finder(contour):
    """Finds the center and diameter of a circle in an image, given that the
    contour provided describes a vaguely circular shape. This algorithm will
    find a correct center regardless of inbalanced density of points. For
    example, if the contour has a bunch more points in the top-left corner than
    in the bottom right, it will not shift the percieved center towards the
    top-left. This makes it better for center-finding than a simple averaging
    of all the contour's points.
    """
    # How it works:
    # 1. Draw a bounding box around all contour points.
    # 2. Divide the bounding box into four quadrants.
    # 3. Seperate all points based on which quadrant they are in.
    # 4. Find one point in each quadrant that is the farthest from the center
    #    of the bounding box (by pythagorean distance)
    # 5. For each of those points, find the point in the opposite quadrant
    #    that is the farthest from it.
    # 6. Draw lines between each starting point and its corresponding farthest
    #    point.
    # 7. Find the line with the highest length. That is the diameter.
    # 8. Find the centerpoint of that line. That is the center.

    # 1. Draw bounding box
    bx1, bx2, by1, by2 = 1000, 0, 1000, 0
    for point in contour:
        x, y = point[0]
        if (x < bx1):
            bx1 = x
        if (x > bx2):
            bx2 = x
        if (y < by1):
            by1 = y
        if (y > by2):
            by2 = y
    
    # 2: Divide box into four quadrants.
    class Quadrant:
        def __init__(self):
            self.points = []
        
        def add_point(self, x, y):
            self.points.append((x, y))
        
        def get_farthest_point(self, x = 0, y = 0):
            if (len(self.points) == 0):
                return None
            farthest_distance = 0
            farthest_index = 0
            for i, point in enumerate(self.points):
                distance = sqrt((point[0] - x) ** 2 + (point[1] - y) ** 2)
                if (distance > farthest_distance):
                    farthest_distance = distance
                    farthest_index = i
            return self.points[farthest_index]
    quadrants = [Quadrant() for i in range(4)]
    bxc, byc = (bx1 + bx2) / 2, (by1 + by2) / 2
                
    # 3: Divide all points into qudrants.
    for point in contour:
        x, y = point[0]
        if (x > bxc):
            if (y > byc):
                quadrants[0].add_point(x, y)
            else:
                quadrants[3].add_point(x, y)
        else:
            if (y > byc):
                quadrants[1].add_point(x, y)
            else:
                quadrants[2].add_point(x, y)
    
    # 4: Find farthest point from center in each quadrant.
    for quadrant in quadrants:
        quadrant.farthest_point = quadrant.get_farthest_point(bxc, byc)
    
    # 5: Find point in opposite quadrant with farthest distance.
    for i in range(len(quadrants)):
        quadrant = quadrants[i]
        opposite = quadrants[(i + len(quadrants) // 2) % len(quadrants)]
        if (quadrant.farthest_point is None):
            continue
        x, y = quadrant.farthest_point
        quadrant.opposite_point = opposite.get_farthest_point(x, y)
    
    # 7: Find lines.
    class Line:
        def __init__(self, point1, point2):
            self.x1, self.y1 = point1
            self.x2, self.y2 = point2
            self.xc, self.yc = (self.x1 + self.x2) // 2, (self.y1 + self.y2) // 2
            self.d = sqrt((self.x1 - self.x2) ** 2 + (self.y1 - self.y2) ** 2)
    lines = []
    for quadrant in quadrants:
        try:
            lines.append(Line(quadrant.farthest_point, quadrant.opposite_point))
        except:
            pass # There was no farthest / opposite point computed for the quadrant.

    # 8: Find longest, and diameter.
    longest = max(lines, key = lambda e: e.d)
    diameter = longest.d

    # 9. Find center point.
    centerx = longest.xc
    centery = longest.yc

    class CircleReport:
        pass
    
    r = CircleReport()
    r.centerx = centerx
    r.centery = centery
    r.diameter = diameter
    r.quadrants = quadrants
    r.bounding_box = SimpleNamespace(
        x1 = bx1, x2 = bx2, y1 = by1, y2 = by2
    )
    r.line = longest
    
    return r

def draw_circle_report(img, circle_report):
    if (circle_report is None):
        return img

    bb = circle_report.bounding_box
    cv2.rectangle(img, (bb.x1, bb.y1), (bb.x2, bb.y2), (127, 127, 127), 1)
    cx, cy = (bb.x1 + bb.x2) // 2, (bb.y1 + bb.y2) // 2
    cv2.line(img, (cx, bb.y1), (cx, bb.y2), (127, 127, 127), 1)
    cv2.line(img, (bb.x1, cy), (bb.x2, cy), (127, 127, 127), 1)

    for quadrant in circle_report.quadrants:
        for x, y in quadrant.points:
            cv2.drawMarker(img, (x, y), (127, 127, 127), cv2.MARKER_SQUARE, 4)

    for quadrant in circle_report.quadrants:
        try:
            cv2.drawMarker(img, quadrant.farthest_point, (0, 0, 255), cv2.MARKER_TRIANGLE_UP, 6)
            cv2.drawMarker(img, quadrant.opposite_point, (255, 0, 0), cv2.MARKER_TRIANGLE_DOWN, 6)
        except:
            pass # No special points computed for quadrant.
    
    l = circle_report.line
    cv2.line(img, (l.x1, l.y1), (l.x2, l.y2), (0, 255, 0), 1)
    c = (circle_report.centerx, circle_report.centery)
    cv2.drawMarker(img, c, (0, 255, 0), cv2.MARKER_DIAMOND, 10)
    cv2.drawMarker(img, c, (0, 255, 0), cv2.MARKER_TILTED_CROSS, 10)
    cv2.circle(img, c, int(circle_report.diameter / 2), (0, 255, 0), 1)

    return img

"""
TODO: Find center of mass of blob contour. I'll need to do some math logicing.
"""

import cv2
import numpy
import math
from enum import Enum

class GripPipeline:
    """
    An OpenCV pipeline generated by GRIP.
    """
    
    def __init__(self):
        """initializes all values to presets or None if need to be set
        """

        self.__hsv_threshold_0_hue = [0.0, 23.344709897610933]
        self.__hsv_threshold_0_saturation = [82.55395683453237, 255.0]
        self.__hsv_threshold_0_value = [255.0, 255.0]

        self.hsv_threshold_0_output = None


        self.__hsv_threshold_1_hue = [0.0, 20.273037542662117]
        self.__hsv_threshold_1_saturation = [170.0, 255.0]
        self.__hsv_threshold_1_value = [150.0, 255.0]

        self.hsv_threshold_1_output = None

        self.__cv_max_src1 = self.hsv_threshold_0_output
        self.__cv_max_src2 = self.hsv_threshold_1_output

        self.cv_max_output = None

        self.__blur_input = self.cv_max_output
        self.__blur_type = BlurType.Box_Blur
        self.__blur_radius = 16.216216216216196

        self.blur_output = None

        self.__cv_threshold_src = self.blur_output
        self.__cv_threshold_thresh = 77.0
        self.__cv_threshold_maxval = 9999.0
        self.__cv_threshold_type = cv2.THRESH_BINARY

        self.cv_threshold_output = None

        self.__find_contours_input = self.cv_threshold_output
        self.__find_contours_external_only = False

        self.find_contours_output = None

        self.__convex_hulls_contours = self.find_contours_output

        self.convex_hulls_output = None

        self.total_convex_hull = None
        self.circle_report = None


    def process(self, source0):
        """
        Runs the pipeline and sets all outputs to new values.
        """
        # Step HSV_Threshold0:
        self.__hsv_threshold_0_input = source0
        (self.hsv_threshold_0_output) = self.__hsv_threshold(self.__hsv_threshold_0_input, self.__hsv_threshold_0_hue, self.__hsv_threshold_0_saturation, self.__hsv_threshold_0_value)

        # Step HSV_Threshold1:
        self.__hsv_threshold_1_input = source0
        (self.hsv_threshold_1_output) = self.__hsv_threshold(self.__hsv_threshold_1_input, self.__hsv_threshold_1_hue, self.__hsv_threshold_1_saturation, self.__hsv_threshold_1_value)

        # Step CV_max0:
        self.__cv_max_src1 = self.hsv_threshold_0_output
        self.__cv_max_src2 = self.hsv_threshold_1_output
        (self.cv_max_output) = self.__cv_max(self.__cv_max_src1, self.__cv_max_src2)

        # Step Blur0:
        self.__blur_input = self.cv_max_output
        (self.blur_output) = self.__blur(self.__blur_input, self.__blur_type, self.__blur_radius)

        # Step CV_Threshold0:
        self.__cv_threshold_src = self.blur_output
        (self.cv_threshold_output) = self.__cv_threshold(self.__cv_threshold_src, self.__cv_threshold_thresh, self.__cv_threshold_maxval, self.__cv_threshold_type)

        # Step Find_Contours0:
        self.__find_contours_input = self.cv_threshold_output
        (self.find_contours_output) = self.__find_contours(self.__find_contours_input, self.__find_contours_external_only)

        # Step Convex_Hulls0:
        # self.__convex_hulls_contours = self.find_contours_output
        # (self.convex_hulls_output) = self.__convex_hulls(self.__convex_hulls_contours)

        if (len(self.find_contours_output)):
            total_contour = numpy.concatenate(self.find_contours_output)
            #(self.total_convex_hull) = self.__total_convex_hull(total_contour)
            self.total_convex_hull = total_contour
            self.circle_report = circle_finder(self.total_convex_hull)
        else:
            print('No contours detected.')
            self.circle_report = None

    @staticmethod
    def __hsv_threshold(input, hue, sat, val):
        """Segment an image based on hue, saturation, and value ranges.
        Args:
            input: A BGR numpy.ndarray.
            hue: A list of two numbers the are the min and max hue.
            sat: A list of two numbers the are the min and max saturation.
            lum: A list of two numbers the are the min and max value.
        Returns:
            A black and white numpy.ndarray.
        """
        out = cv2.cvtColor(input, cv2.COLOR_BGR2HSV)
        return cv2.inRange(out, (hue[0], sat[0], val[0]),  (hue[1], sat[1], val[1]))

    @staticmethod
    def __cv_max(src1, src2):
        """Performs a per element max.
        Args:
            src1: A numpy.ndarray.
            src2: A numpy.ndarray.
        Returns:
            The max as a numpy.ndarray.
        """
        return cv2.max(src1, src2)

    @staticmethod
    def __blur(src, type, radius):
        """Softens an image using one of several filters.
        Args:
            src: The source mat (numpy.ndarray).
            type: The blurType to perform represented as an int.
            radius: The radius for the blur as a float.
        Returns:
            A numpy.ndarray that has been blurred.
        """
        if(type is BlurType.Box_Blur):
            ksize = int(2 * round(radius) + 1)
            return cv2.blur(src, (ksize, ksize))
        elif(type is BlurType.Gaussian_Blur):
            ksize = int(6 * round(radius) + 1)
            return cv2.GaussianBlur(src, (ksize, ksize), round(radius))
        elif(type is BlurType.Median_Filter):
            ksize = int(2 * round(radius) + 1)
            return cv2.medianBlur(src, ksize)
        else:
            return cv2.bilateralFilter(src, -1, round(radius), round(radius))

    @staticmethod
    def __cv_threshold(src, thresh, max_val, type):
        """Apply a fixed-level threshold to each array element in an image
        Args:
            src: A numpy.ndarray.
            thresh: Threshold value.
            max_val: Maximum value for THRES_BINARY and THRES_BINARY_INV.
            type: Opencv enum.
        Returns:
            A black and white numpy.ndarray.
        """
        return cv2.threshold(src, thresh, max_val, type)[1]

    @staticmethod
    def __find_contours(input, external_only):
        """Sets the values of pixels in a binary image to their distance to the nearest black pixel.
        Args:
            input: A numpy.ndarray.
            external_only: A boolean. If true only external contours are found.
        Return:
            A list of numpy.ndarray where each one represents a contour.
        """
        if(external_only):
            mode = cv2.RETR_EXTERNAL
        else:
            mode = cv2.RETR_LIST
        method = cv2.CHAIN_APPROX_SIMPLE
        im2, contours, hierarchy =cv2.findContours(input, mode=mode, method=method)
        return contours

    @staticmethod
    def __convex_hulls(input_contours):
        """Computes the convex hulls of contours.
        Args:
            input_contours: A list of numpy.ndarray that each represent a contour.
        Returns:
            A list of numpy.ndarray that each represent a contour.
        """
        output = []
        for contour in input_contours:
            output.append(cv2.convexHull(contour))
        return output

    @staticmethod
    def __total_convex_hull(input_contours):
        """Computes the convex hulls of all contours as a whole.
        Args:
            input_contours: A list of numpy.ndarray that each represent a contour.
        Returns:
            A numpy.ndarray that represent the convex hull (contour).
        """
        return cv2.convexHull(sum(input_contours))


BlurType = Enum('BlurType', 'Box_Blur Gaussian_Blur Median_Filter Bilateral_Filter')


# Code copied from Python example server.
#----------------------------------------------------------------------------
# Copyright (c) 2018 FIRST. All Rights Reserved.
# Open Source Software - may be modified and shared by FRC teams. The code
# must be accompanied by the FIRST BSD license file in the root directory of
# the project.
#----------------------------------------------------------------------------

import json
import time
import sys

from cscore import CameraServer, VideoSource, UsbCamera, MjpegServer
from networktables import NetworkTablesInstance

#   JSON format:
#   {
#       "team": <team number>,
#       "ntmode": <"client" or "server", "client" if unspecified>
#       "cameras": [
#           {
#               "name": <camera name>
#               "path": <path, e.g. "/dev/video0">
#               "pixel format": <"MJPEG", "YUYV", etc>   // optional
#               "width": <video mode width>              // optional
#               "height": <video mode height>            // optional
#               "fps": <video mode fps>                  // optional
#               "brightness": <percentage brightness>    // optional
#               "white balance": <"auto", "hold", value> // optional
#               "exposure": <"auto", "hold", value>      // optional
#               "properties": [                          // optional
#                   {
#                       "name": <property name>
#                       "value": <property value>
#                   }
#               ],
#               "stream": {                              // optional
#                   "properties": [
#                       {
#                           "name": <stream property name>
#                           "value": <stream property value>
#                       }
#                   ]
#               }
#           }
#       ]
#   }

configFile = "/boot/frc.json"

class CameraConfig: pass

team = None
server = False
cameraConfigs = []

def parseError(str):
    """Report parse error."""
    print("config error in '" + configFile + "': " + str, file=sys.stderr)

def readCameraConfig(config):
    """Read single camera configuration."""
    cam = CameraConfig()

    # name
    try:
        cam.name = config["name"]
    except KeyError:
        parseError("could not read camera name")
        return False

    # path
    try:
        cam.path = config["path"]
    except KeyError:
        parseError("camera '{}': could not read path".format(cam.name))
        return False

    # stream properties
    cam.streamConfig = config.get("stream")

    cam.config = config

    cameraConfigs.append(cam)
    return True

def readConfig():
    """Read configuration file."""
    global team
    global server

    # parse file
    try:
        with open(configFile, "rt") as f:
            j = json.load(f)
    except OSError as err:
        print("could not open '{}': {}".format(configFile, err), file=sys.stderr)
        return False

    # top level must be an object
    if not isinstance(j, dict):
        parseError("must be JSON object")
        return False

    # team number
    try:
        team = j["team"]
    except KeyError:
        parseError("could not read team number")
        return False

    # ntmode (optional)
    if "ntmode" in j:
        str = j["ntmode"]
        if str.lower() == "client":
            server = False
        elif str.lower() == "server":
            server = True
        else:
            parseError("could not understand ntmode value '{}'".format(str))

    # cameras
    try:
        cameras = j["cameras"]
    except KeyError:
        parseError("could not read cameras")
        return False
    for camera in cameras:
        if not readCameraConfig(camera):
            return False

    return True

def startCamera(config):
    """Start running the camera."""
    print("Starting camera '{}' on {}".format(config.name, config.path))
    inst = CameraServer.getInstance()
    camera = UsbCamera(config.name, config.path)
    server = inst.startAutomaticCapture(camera=camera, return_server=True)

    camera.setConfigJson(json.dumps(config.config))
    camera.setConnectionStrategy(VideoSource.ConnectionStrategy.kKeepOpen)

    if config.streamConfig is not None:
        server.setConfigJson(json.dumps(config.streamConfig))

    camInput = inst.getVideo()
    img = numpy.zeros(shape=(240, 320, 3), dtype=numpy.uint8)
    pipeline = GripPipeline()
    thresh1 = inst.putVideo("Img Proc: Threshold #1", 320, 240)
    thresh2 = inst.putVideo("Img Proc: Threshold #2", 320, 240)
    blur = inst.putVideo("Img Proc: Blur", 320, 240)
    thresh3 = inst.putVideo("Img Proc: Threshold #3", 320, 240)
    circle_report = inst.putVideo("Img Proc: Circle Finder", 320, 240)

    while True:
        time, img = camInput.grabFrame(img)
        if (time == 0):
            print('There was an error retrieving the frame.')
            continue
        pipeline.process(img)
        thresh1.putFrame(pipeline.hsv_threshold_0_output)
        thresh2.putFrame(pipeline.hsv_threshold_1_output)
        blur.putFrame(pipeline.blur_output)
        thresh3.putFrame(pipeline.cv_threshold_output)
        circle_report.putFrame(draw_circle_report(img, pipeline.circle_report))

    return camera

if __name__ == "__main__":
    if len(sys.argv) >= 2:
        configFile = sys.argv[1]

    # read configuration
    if not readConfig():
        sys.exit(1)

    # start NetworkTables
    ntinst = NetworkTablesInstance.getDefault()
    if server:
        print("Setting up NetworkTables server")
        ntinst.startServer()
    else:
        print("Setting up NetworkTables client for team {}".format(team))
        ntinst.startClientTeam(team)

    # start cameras
    cameras = []
    for cameraConfig in cameraConfigs:
        cameras.append(startCamera(cameraConfig))

    # loop forever
    while True:
        time.sleep(10)

