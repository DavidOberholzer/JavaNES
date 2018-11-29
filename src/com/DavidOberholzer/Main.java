package com.DavidOberholzer;

public class Main {

    public static void main(String[] args) {
        CPU_6502 cpu = new CPU_6502();
        cpu.runOpCode(0x69, 0xff);
        System.out.println("A value: " + cpu.getA());
        cpu.printStatus();
        cpu.runOpCode(0x69, 0x01);
        System.out.println("A value: " + cpu.getA());
        cpu.printStatus();
        cpu.runOpCode(0x69, 0x01);
        System.out.println("A value: " + cpu.getA());
        cpu.printStatus();
        cpu.runOpCode(0x69, 0x01);
        System.out.println("A value: " + cpu.getA());
        cpu.printStatus();
        cpu.runOpCode(0x69, 126);
        System.out.println("A value: " + cpu.getA());
        cpu.printStatus();

        System.out.println("bit shift left " + (0x04 << 2));
    }
}
