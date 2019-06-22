// Motor A
int enA = 3;
int in1 = 2;
int in2 = 4;

// Motor B
int enB = 9;
int in3 = 7;
int in4 = 8;

const char CMD_SEP = ';';
String cmdBuffer = "";

void setup() {
  // Set all the motor control pins to outputs
  pinMode(enA, OUTPUT);
  pinMode(enB, OUTPUT);
  pinMode(in1, OUTPUT);
  pinMode(in2, OUTPUT);
  pinMode(in3, OUTPUT);
  pinMode(in4, OUTPUT);

  analogWrite(enA, 255);
  analogWrite(enB, 255);

  Serial.begin(9600);
  Serial.println("Init system completed.");
}

void loop() {
  while (Serial.available() > 0) {
    char c = Serial.read();
    Serial.print(c);
    if (c == CMD_SEP) {
      processCommand();
      cmdBuffer = "";
    } else {
      cmdBuffer += c;
    }
  }
}

void processCommand() {
  if (cmdBuffer == "L") {
    turnRight();
  } else if (cmdBuffer == "R") {
    turnLeft();
  } else if (cmdBuffer == "T") {
    goStraightHead();
  } else if (cmdBuffer == "B") {
    goBackward();
  } else if (cmdBuffer == "S") {
    stopMotor();
  } else if (cmdBuffer.charAt(0) == 'V') {
    processSpeedCommand();
  }
}

void processSpeedCommand() {
  String sSpeed = cmdBuffer.substring(2); // A
  int speed = atoi(sSpeed.c_str());
  analogWrite(enA, speed);
  analogWrite(enB, speed);
}

void goStraightHead() {
  // UP LEFT
  digitalWrite(in1, LOW);
  digitalWrite(in2, HIGH);

  // UP RIGHT
  digitalWrite(in3, HIGH);
  digitalWrite(in4, LOW);
}

void goBackward() {
  // DOWN LEFT
  digitalWrite(in1, HIGH);
  digitalWrite(in2, LOW);

  // DOWN RIGHT
  digitalWrite(in3, LOW);
  digitalWrite(in4, HIGH);
}

void turnRight() {
  digitalWrite(in1, LOW);
  digitalWrite(in2, HIGH);

  digitalWrite(in3, LOW);
  digitalWrite(in4, HIGH);
}

void turnLeft() {

  // DOWN LEFT
  digitalWrite(in1, HIGH);
  digitalWrite(in2, LOW);

  // UP
  digitalWrite(in3, HIGH);
  digitalWrite(in4, LOW);
}

void stopMotor() {
  digitalWrite(in1, LOW);
  digitalWrite(in2, LOW);
  digitalWrite(in3, LOW);
  digitalWrite(in4, LOW);
}
//
//void demoOne()
//
//{
//
//  // This function will run the motors in both directions at a fixed speed
//
//  // Turn on motor A
//
//  digitalWrite(in1, HIGH);
//  digitalWrite(in2, LOW);
//
//  // Set speed to 200 out of possible range 0~255
//
//  analogWrite(enA, 200);
//
//  // Turn on motor B
//
//  digitalWrite(in3, HIGH);
//  digitalWrite(in4, LOW);
//
//  // Set speed to 200 out of possible range 0~255
//
//  analogWrite(enB, 200);
//
//  delay(2000);
//
//  // Now change motor directions
//
//  digitalWrite(in1, LOW);
//  digitalWrite(in2, HIGH);
//  digitalWrite(in3, LOW);
//  digitalWrite(in4, HIGH);
//
//  delay(2000);
//
//  // Now turn off motors
//
//  digitalWrite(in1, LOW);
//  digitalWrite(in2, LOW);
//  digitalWrite(in3, LOW);
//  digitalWrite(in4, LOW);
//
//}
//
//void demoTwo()
//
//{
//
//  // This function will run the motors across the range of possible speeds
//  // Note that maximum speed is determined by the motor itself and the operating voltage
//
//  // Turn on motors
//
//  digitalWrite(in1, LOW);
//  digitalWrite(in2, HIGH);
//  digitalWrite(in3, LOW);
//  digitalWrite(in4, HIGH);
//
//  // Accelerate from zero to maximum speed
//
//  for (int i = 0; i < 256; i++)
//
//  {
//
//    analogWrite(enA, i);
//    analogWrite(enB, i);
//
//    delay(20);
//
//  }
//
//  // Decelerate from maximum speed to zero
//
//  for (int i = 255; i >= 0; --i)
//
//  {
//
//    analogWrite(enA, i);
//    analogWrite(enB, i);
//
//    delay(20);
//
//  }
//
//  // Now turn off motors
//
//  digitalWrite(in1, LOW);
//  digitalWrite(in2, LOW);
//  digitalWrite(in3, LOW);
//  digitalWrite(in4, LOW);
//
//}
