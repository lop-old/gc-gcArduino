//###################################################
//# This file is part of ArduinoGC for GrowControl
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



// scan for devices
void commandScan() {
  delay( SCAN_DELAY * (OWN_ID + 2) );
  Serial << padInt(OWN_ID, SIZE_ID) << "id" << VERSION << LINE_ENDING;
}



// set all pins
void commandAll() {
  String arg = getArgument(OFFSET_ARG1, SIZE_ARG);
  if (getArgument(OFFSET_ARG1, SIZE_ARG - 1).equals("LOW")) {
    arg = "LOW";
  }
  // set all to output mode off/low
  if (arg.equals("OUTP") || arg.equals("LOW_")) {
    for (int i = PIN_FROM; i < PIN_TO; i++) {
      pinMode     (i, OUTPUT);
      digitalWrite(i, LOW);
    }
  } else
  // set all to output mode on/high
  if (arg.equals("HIGH")) {
    for (int i = PIN_FROM; i < PIN_TO; i++) {
      pinMode     (i, OUTPUT);
      digitalWrite(i, HIGH);
    }
  } else
  // set all to input mode
  if (arg.equals("INPU")) {
    for (int i = PIN_FROM; i < PIN_TO; i++)
      pinMode(i, INPUT);
  } else
  // set all to input pulled high mode
  if (arg.equals("INHI")) {
    for (int i = PIN_FROM; i < PIN_TO; i++)
      pinMode(i, INPUT_PULLUP);
  } else {
    // error
    Serial << padInt(OWN_ID, SIZE_ID) << "ERROR" << LINE_ENDING;
    return;
  }
  Serial << padInt(OWN_ID, SIZE_ID) << "al" << arg << LINE_ENDING;
}



// set pin mode
void commandPinMode() {
  int    pin  = getIntArgument(OFFSET_ARG1, SIZE_ARG);
  String mode = getArgument   (OFFSET_ARG2, SIZE_ARG);
  mode.toUpperCase();
  // set output mode
  if (mode.equals("OUTP")) {
    pinMode     (pin, OUTPUT);
    digitalWrite(pin, LOW);
  } else
  // set input floating mode
  if (mode.equals("INPU")) {
    pinMode     (pin, INPUT);
    digitalWrite(pin, LOW);
  } else
  // set input pulled high
  if (mode.equals("INHI")) {
    pinMode     (pin, INPUT_PULLUP);
    //digitalWrite(pin, HIGH);
  } else {
    // error
    Serial << padInt(OWN_ID, SIZE_ID) << "ERRORpm" << LINE_ENDING;
    return;
  }
  Serial << padInt(OWN_ID, SIZE_ID) << "pm" << padInt(pin, SIZE_ARG) << mode << LINE_ENDING;
}



// digital write
void commandDigitalWrite() {
  int  pin = getIntArgument(OFFSET_ARG1, SIZE_ARG);
  char arg = command.charAt(OFFSET_ARG2);
  // set pin high
  if (arg == '1' || arg == 'H' || arg == 'h') {
    digitalWrite(pin, HIGH);
    Serial << padInt(OWN_ID, SIZE_ID) << "dw" << padInt(pin, SIZE_ARG) << "HIGH" << LINE_ENDING;
  } else
  // set pin low
  if (arg == '0' || arg == 'L' || arg == 'l') {
    digitalWrite(pin, LOW);
    Serial << padInt(OWN_ID, SIZE_ID) << "dw" << padInt(pin, SIZE_ARG) << "LOW_" << LINE_ENDING;
  } else {
    // error
    Serial << padInt(OWN_ID, SIZE_ID) << "ERRORdw" << LINE_ENDING;
  }
}



// pwm
void commandAnalogWrite() {
  int pin = getIntArgument(OFFSET_ARG1, SIZE_ARG);
  int val = getIntArgument(OFFSET_ARG2, SIZE_ARG);
  analogWrite(pin, val);
  Serial << padInt(OWN_ID, SIZE_ID) << "aw" << padInt(pin, SIZE_ARG) << padInt(val, SIZE_ARG) << LINE_ENDING;
}



// digital read
void commandDigitalRead() {
  int pin = getIntArgument(OFFSET_ARG1, SIZE_ARG);
  int val = digitalRead(pin);
  Serial << padInt(OWN_ID, SIZE_ID) << "dr" << padInt(pin, SIZE_ARG) << padInt(val, SIZE_ARG) << LINE_ENDING;
}



// analog read
void commandAnalogRead() {
  int pin = getIntArgument(OFFSET_ARG1, SIZE_ARG);
  // read analog
  int val = analogRead(pin);
  Serial << padInt(OWN_ID, SIZE_ID) << "ar" << padInt(pin, SIZE_ARG) << padInt(val, SIZE_ARG) << LINE_ENDING;
}



void commandId() {
  int id = getIntArgument(OFFSET_ARG1, SIZE_ARG);
  if (id < 0 || id > MAX_ID) {
    Serial << padInt(OWN_ID, SIZE_ID) << "ERRORid" << LINE_ENDING;
    return;
  }
  // save to flash
#if defined ( is_AVR )
  EEPROM.write(MEMADDR_ID, id);
#elif defined ( is_SAM )
  DueFlashStorage().write(MEMADDR_ID, (byte) id);
#endif
  OWN_ID = id;
  Serial << padInt(OWN_ID, SIZE_ID) << "id" << LINE_ENDING;
}

