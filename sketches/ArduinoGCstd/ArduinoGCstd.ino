//###################################################
//#    Controller Software
//#      for GrowControl
//#
//# Supported software versions:
//#   GrowControl 3.5
//#
//# Version:
//#   1.5.0 - Initial release based on SerialControl sketch.
//#
//# This is free software: you may redistribute
//# and/or modify it under the terms of the GNU
//# General Public License as published by the Free
//# Software Foundation, either version 2 of the
//# license, or (at your option) any later version.
//# This software is distributed in the hope that it
//# will be useful, but WITHOUT ANY WARRANTY; without
//# even the implied warranty of MERCHANTABILITY or
//# FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//# General Public License for more details. You
//# should have received a copy of the GNU General
//# Public License along with SerialControl.  If not,
//# see <http://www.gnu.org/licenses/>.
//#
//###################################################
# define VERSION "1.5.0"



// uncomment only one (if not auto-detected)

// Arduino Uno
//#define is_AVR

// Arduino Due
//#define is_SAM



//#define  FORCE_ID  00



#define  BAUD_RATE   9600
#define  SCAN_DELAY  50
#define  LINE_ENDING  ";\r\n"



#define  PIN_FROM  2
#define  PIN_TO    13



/****************************************************

Command Structure
=================
pos: 12 34 5678 9012 345
     ID CM ARG1 ARG2 ;RN
     01 23 4567 8901 234

  ID | device id
  CM | command
ARG1 | argument 1
ARG2 | argument 2
  RN | line ending


Commands
========
scan | request id number for all connected devices
pm | set pin mode
dw | digital write
aw | analog write
dr | digital read
ar | analog read
al | set all pins
id | set the device id in eeprom


Pin Modes
=========
OUTP | output mode (io or pwm)
INPU | input mode (floating, external resistor)
INHI | input mode (pulled high internally)


Error Codes
===========
ERRORcm | invalid command
ERRORpm | invalid pin mode
ERRORdw | invalid output state

****************************************************/



// eeprom address locations
#define  MEMADDR_ID  11

// packet structure
#define  OFFSET_ID    0
#define  OFFSET_CMD   2
#define  OFFSET_ARG1  4
#define  OFFSET_ARG2  8
#define  SIZE_ID   2
#define  SIZE_ARG  4

#define  MAX_ID 99



// auto-detect mcu type
#if !defined ( is_AVR ) && !defined ( is_SAM )

// Arduino Uno
#if defined ( __AVR_ATmega168__ ) || defined ( __AVR_ATmega328P__ )
#define is_AVR
#endif

// Arduino Due
#ifdef _LIB_SAM_
#define is_SAM
#endif

#endif



#ifdef is_AVR
#include <EEPROM.h>
#endif
#ifdef is_SAM
#include <DueFlashStorage.h>
#endif



template<class T> inline Print &operator << (Print &obj, T arg) { obj.print(arg); return obj; }



//const byte PINS[]    = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};



// internal variables
int OWN_ID  = 0;
int CALL_ID = -1;
String command = String();



void setup() {
#ifdef FORCE_ID
  OWN_ID = FORCE_ID;
#elif defined ( is_AVR )
  OWN_ID = EEPROM.read( MEMADDR_ID );
#elif defined ( is_SAM )
  OWN_ID = (int) DueFlashStorage().read( MEMADDR_ID );
#endif
  if (OWN_ID < 0 || OWN_ID > MAX_ID) {
    OWN_ID = 0;
  }
  Serial.begin( BAUD_RATE );
  // announce device exists
  commandScan();
  command = "##alOUTP";
  commandAll();
  clearBuffer();
}



void loop() {
  getIncomingChars();
}



// reset serial input buffer
void clearBuffer() {
  command = "";
  CALL_ID = -1;
}



void getIncomingChars() {
  while (Serial.available() > 0) {
    char inChar = Serial.read();
    for (int i = 0; i < sizeof(LINE_ENDING); i++) {
      if (LINE_ENDING[i] == inChar) {
        processCommand();
        return;
      }
    }
    command += inChar;
  }
}



void processCommand() {
  if (command.length() == 0) {
    clearBuffer();
    return;
  }
  // scanning for devices
  if (getArgument(0, 4).equals("scan")) {
    commandScan();
    clearBuffer();
    return;
  }
  // validate id
  for (int i = 0; i < SIZE_ID; i++) {
    if (!isNumeric(command.charAt(i))) {
      clearBuffer();
      return;
    }
  }
  CALL_ID = getIntArgument(OFFSET_ID, SIZE_ID);
  // calling this device
  if (CALL_ID != OWN_ID) {
    clearBuffer();
    return;
  }
  // pm - set pin mode
  if (command.charAt(OFFSET_CMD) == 'p' && command.charAt(OFFSET_CMD + 1) == 'm') {
    commandPinMode();
  } else
  // dw - digital write
  if (command.charAt(OFFSET_CMD) == 'd' && command.charAt(OFFSET_CMD + 1) == 'w') {
    commandDigitalWrite();
  } else
  // aw - analog write
  if (command.charAt(OFFSET_CMD) == 'a' && command.charAt(OFFSET_CMD + 1) == 'w') {
    commandAnalogWrite();
  } else
  // dr - digital read
  if (command.charAt(OFFSET_CMD) == 'd' && command.charAt(OFFSET_CMD + 1) == 'r') {
    commandDigitalRead();
  } else
  // ar - analog read
  if (command.charAt(OFFSET_CMD) == 'a' && command.charAt(OFFSET_CMD + 1) == 'r') {
    commandAnalogRead();
  } else
  // al - set all pins
  if (command.charAt(OFFSET_CMD) == 'a' && command.charAt(OFFSET_CMD + 1) == 'l') {
    commandAll();
  } else
  // id - set the device id in eeprom
  if (command.charAt(OFFSET_CMD) == 'i' && command.charAt(OFFSET_CMD + 1) == 'd') {
    commandId();
  } else {
    Serial << padInt(OWN_ID, SIZE_ID) << "ERRORCM" << LINE_ENDING;
  }
}



String getArgument(int offset, int width) {
  return command.substring(offset, offset + width);
}



int getIntArgument(int offset, int width) {
  int val = 0;
  for (int i = offset; i < offset + width; i++) {
    if(isNumeric(command.charAt(i))) {
      val *= 10;
      val += (command.charAt(i) - '0');
    }
  }
  return val;
}



boolean isNumeric(char character) {
  return (character >= '0' && character <= '9');
}



String padInt(int val, byte width) {
  String buf = String();
  int pow = 1;
  for (byte b = 1; b < width; b++) {
    pow *= 10;
    if(val < pow)
      buf += '0';
  }
  return buf + val;
}

