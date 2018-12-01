package com.DavidOberholzer;

public class Main {

    public static void main(String[] args) {
        CPU_6502 cpu = new CPU_6502();
        cpu.runOpCode(0x69, 0xff, 0x00);
        System.out.println("A value: " + cpu.getA());
        cpu.runOpCode(0x69, 0x01, 0x01);
        System.out.println("A value: " + cpu.getA());
        cpu.printStatus();
        cpu.runOpCode(0x69, 0x01, 0x02);
        System.out.println("A value: " + cpu.getA());
        cpu.printStatus();
        cpu.runOpCode(0x69, 0x01, 0x03);
        System.out.println("A value: " + cpu.getA());
        cpu.printStatus();
        cpu.runOpCode(0x69, 126, 0x04);
        System.out.println("A value: " + cpu.getA());
        cpu.printStatus();

        System.out.println("bit shift left " + (0x04 << 2));
    }
}
