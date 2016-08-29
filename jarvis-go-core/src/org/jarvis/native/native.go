package syscall

// #ifdef WIRINGPI
// #cgo amd64 386 CFLAGS: -DMOCK
// #else
// #cgo LDFLAGS:
// void  delay             	(unsigned int howLong) {}
// void  delayMicroseconds 	(unsigned int howLong) {}
// unsigned int millis      (void) {}
// unsigned int micros      (void) {}
// void digitalWrite        (int pin, int value) {}
// int  wiringPiSetup       (void) {return 0;}
// void pinMode             (int pin, int mode) {}
// int  setuid      		(int uid) {return 0;}
// #endif
// #include "native.h"
//import "C"

import (
	"syscall"
)

func Setuid(uid int) (err error) {
	return syscall.EOPNOTSUPP
}

func InitWiringPi() int {
	Setuid(0)
	return 0
}

func DioOn(pin int, sender int, interruptor int) int {
	return int(0)
}

func DioOff(pin int, sender int, interruptor int) int {
	return int(0)
}

/*
func InitWiringPi() int {
	return int(C.initWiringPi())
}

func DioOn(pin int, sender int, interruptor int) int {
	return int(C.dioOn(C.int(pin), C.int(sender), C.int(interruptor)))
}

func DioOff(pin int, sender int, interruptor int) int {
	return int(C.dioOff(C.int(pin), C.int(sender), C.int(interruptor)))
}
*/
