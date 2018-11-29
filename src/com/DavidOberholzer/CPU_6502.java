package com.DavidOberholzer;

public class CPU_6502 {
    private StatusRegister Status;
    private int A, X, Y;
    private int zeroPage[] = new int[256];
    private int memoryMap[] = new int[65280];

    public CPU_6502() {
        Status = new StatusRegister();
        A = 0x00;
        X = 0x00;
        Y = 0x00;
        zeroPage[0x7D] = 0x76;
    }

    public int runOpCode(int opCode, int value, int currentInstruction) {
        if (opCode > 0xFF) {
            System.out.println("INVALID OPCODE, OUT OF BOUNDS! EXITING...");
            return -1;
        }
        switch (opCode) {
            // ADD WITH CARRY (ADC)
            // ADC Immediate
            case 0x69:
                this.addToA(value);
                break;
            // ADC Zero Page
            case 0x65:
                this.addToA(this.zeroPageAtIndex(value));
                break;
            // ADC Zero Page,X
            case 0x75:
                this.addToA(this.zeroPageAtIndexX(value));
                break;
            // ADC Absolute
            case 0x6D:
                this.addToA(memoryMap[value]);
                break;
            // ADC Absolute,X
            case 0x7D:
                this.addToA(memoryMap[value + X]);
                break;
            // ADC Absolute,Y
            case 0x79:
                this.addToA(memoryMap[value + Y]);
                break;
            // ADC Indirect,X
            case 0x61:
                this.addToA(this.indirectX(value));
                break;
            // ADC Indirect,Y
            case 0x71:
                this.addToA(this.indirectY(value));
                break;

            // BITWISE AND WITH ACCUMULATOR (AND)
            // AND Immediate
            case 0x29:
                this.bitwiseAndWithA(value);
                break;
            // AND Zero Page
            case 0x25:
                this.bitwiseAndWithA(this.zeroPageAtIndex(value));
                break;
            // AND Zero Page,X
            case 0x35:
                this.bitwiseAndWithA(this.zeroPageAtIndexX(value));
                break;
            // AND Absolute
            case 0x2D:
                this.bitwiseAndWithA(memoryMap[value]);
                break;
            // AND Absolute,X
            case 0x3D:
                this.bitwiseAndWithA(memoryMap[value] + X);
                break;
            // AND Absolute,Y
            case 0x39:
                this.bitwiseAndWithA(memoryMap[value] + Y);
                break;
            // AND Indirect,X
            case 0x21:
                this.bitwiseAndWithA(this.indirectX(value));
                break;
            // AND Indirect,Y
            case 0x31:
                this.bitwiseAndWithA(this.indirectY(value));
                break;

            // ARITHMETIC SHIFT LEFT (ASL)
            // ASL Accumulator
            case 0x0A:
                this.shiftALeft(1);
                break;
            // ASL Zero Page
            case 0x06:
                this.zeroPageShiftLeft(1, this.zeroPageIndex(value));
                break;
            // ASL Zero Page,X
            case 0x16:
                this.zeroPageShiftLeft(1, this.zeroPageIndexX(value));
                break;
            // ASL Absolute
            case 0x0E:
                this.absoluteShiftLeft(1, value);
                break;
            // ASL Absolute,X
            case 0x1E:
                this.absoluteShiftLeft(1, value + X);
                break;

            // TEST BITS (BIT)
            // BIT Zero Page
            case 0x24:
                this.bitTestWithA(this.zeroPageAtIndex(value));
                break;
            // BIT Absolute
            case 0x2C:
                this.bitTestWithA(memoryMap[value]);
                break;

            // BRANCH INSTRUCTIONS
            // Branch on PLus (BPL)
            case 0x10:
                if (Status.getN() == 0)
                    return value;
                break;
            // Branch on MInus
            case 0x30:
                if (Status.getN() == 1)
                    return value;
                break;
            // Branch on oVerflow Clear (BVC)
            case 0x50:
                if (Status.getV() == 0)
                    return value;
                break;
            // Branch on oVerflow Set (BVS)
            case 0x70:
                if (Status.getV() == 1)
                    return value;
                break;
            // Branch on Carry Clear (BCC)
            case 0x90:
                if (Status.getC() == 0)
                    return value;
                break;
            // Branch on Carry Set (BCS)
            case 0xB0:
                if (Status.getC() == 1)
                    return value;
                break;
            // Branch on Not Equal (BNE)
            case 0xD0:
                if (Status.getZ() == 0)
                    return value;
                break;
            // Branch on EQual (BEQ)
            case 0xF0:
                if (Status.getZ() == 1)
                    return value;
                break;

            // BReaK (BRK)
            case 0x00:
                return 2;

            // COMPARE ACCUMULATOR (CMP)
            // CMP Immediate
            case 0xC9:
                this.compareWithRegister(value, A);
                break;
            // CMP Zero Page
            case 0xC5:
                this.compareWithRegister(zeroPageAtIndex(value), A);
                break;
            // CMP Zero Page,X
            case 0xD5:
                this.compareWithRegister(zeroPageAtIndexX(value), A);
                break;
            // CMP Absolute
            case 0xCD:
                this.compareWithRegister(memoryMap[value], A);
                break;
            // CMP Absolute,X
            case 0xDD:
                this.compareWithRegister(memoryMap[value + X], A);
                break;
            // CMP Absolute,Y
            case 0xD9:
                this.compareWithRegister(memoryMap[value + Y], A);
                break;
            // CMP Indirect,X
            case 0xC1:
                this.compareWithRegister(this.indirectX(value), A);
                break;
            // CMP Indirect,Y
            case 0xD1:
                this.compareWithRegister(this.indirectY(value), A);
                break;

            // COMPARE X REGISTER (CPX)
            // CPX Immediate
            case 0xE0:
                this.compareWithRegister(value, X);
                break;
            // CPX Zero Page
            case 0xE4:
                this.compareWithRegister(this.zeroPageAtIndex(value), X);
                break;
            // CPX Absolute
            case 0xEC:
                this.compareWithRegister(memoryMap[value], X);
                break;

            // COMPARE Y REGISTER (CPY)
            // CPY Immediate
            case 0xC0:
                this.compareWithRegister(value, Y);
                break;
            // CPY Zero Page
            case 0xC4:
                this.compareWithRegister(this.zeroPageAtIndex(value), Y);
                break;
            // CPY Absolute
            case 0xCC:
                this.compareWithRegister(memoryMap[value], Y);
                break;

            // DECREMENT MEMORY (DEC)
            // DEC Zero Page
            case 0xC6:
                this.addToMemory(this.zeroPageIndex(value), zeroPage, -1);
                break;
            // DEC Zero Page,X
            case 0xD6:
                this.addToMemory(this.zeroPageIndex(value), zeroPage, -1);
                break;
            // DEC Absolute
            case 0xCE:
                this.addToMemory(value, memoryMap, -1);
                break;
            // DEC Absolute,X
            case 0xDE:
                this.addToMemory(value + X, memoryMap, -1);
                break;

            // BITWISE EXCLUSIVE OR (EOR)
            // EOR Immediate
            case 0x49:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // EOR Zero Page
            case 0x45:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // EOR Zero Page,X
            case 0x55:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // EOR Absolute
            case 0x4D:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // EOR Absolute,X
            case 0x5D:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // EOR Absolute,Y
            case 0x59:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // EOR Indirect,X
            case 0x41:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // EOR Indirect,Y
            case 0x51:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;

            // FLAG Instructions
            // CLear Carry (CLC)
            case 0x18:
                Status.setC((short) 0);
                break;
            // SEt Carry (SEC)
            case 0x38:
                Status.setC((short) 1);
                break;
            // CLear Interrupt (CLI)
            case 0x58:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // SEt Interrupt (SEI)
            case 0x78:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // CLear oVerflow (CLV)
            case 0xB8:
                Status.setV((short) 0);
                break;
            // CLear Decimal (CLD)
            case 0xD8:
                Status.setD((short) 0);
                break;
            // SEt Decimal (SED)
            case 0xF8:
                Status.setD((short) 1);
                break;

            // INCREMENT MEMORY (INC)
            // INC Zero Page
            case 0xE6:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // ADC Zero Page,X
            case 0xF6:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // ADC Absolute
            case 0xEE:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // ADC Absolute,X
            case 0xFE:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;

            // JUMP (JMP)
            // JMP Absolute
            case 0x4C:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // JMP Indirect
            case 0x6C:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;

            // JUMP TO SUBROUTINE (JSR)
            case 0x20:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;

            // LOAD ACCUMULATOR (LDA)
            // LDA Immediate
            case 0xA9:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // LDA Zero Page
            case 0xA5:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // LDA Zero Page,X
            case 0xB5:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // LDA Absolute
            case 0xAD:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // LDA Absolute,X
            case 0xBD:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // LDA Absolute,Y
            case 0xB9:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // LDA Indirect,X
            case 0xA1:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // LDA Indirect,Y
            case 0xB1:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;

            // LOAD X REGISTER (LDX)
            // LDX Immediate
            case 0xA2:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // LDX Zero Page
            case 0xA6:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // LDX Zero Page,Y
            case 0xB6:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // LDX Absolute
            case 0xAE:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // LDX Absolute,Y
            case 0xBE:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;

            // LOAD Y REGISTER (LDY)
            // LDY Immediate
            case 0xA0:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // LDY Zero Page
            case 0xA4:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // LDY Zero Page,X
            case 0xB4:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // LDY Absolute
            case 0xAC:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // LDY Absolute,X
            case 0xBC:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;

            // LOGICAL SHIFT RIGHT (LSR)
            // LSR Immediate
            case 0x4A:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // LSR Zero Page
            case 0x46:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // LSR Zero Page,X
            case 0x56:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // LSR Absolute
            case 0x4E:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // LSR Absolute,X
            case 0x5E:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;

            // NO OPERATION (NOP)
            case 0xEA:
                break;

            // BITWISE OR WITH ACCUMULATOR (ORA)
            // ORA Immediate
            case 0x09:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // ORA Zero Page
            case 0x05:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // ORA Zero Page,X
            case 0x15:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // ORA Absolute
            case 0x0D:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // ORA Absolute,X
            case 0x1D:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // ORA Absolute,Y
            case 0x19:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // ORA Indirect,X
            case 0x01:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // ORA Indirect,Y
            case 0x11:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;

            // REGISTER INSTRUCTIONS
            // Transfer A to X (TAX)
            case 0xAA:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // Transfer X to A (TXA)
            case 0x8A:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // DEcrement X (DEX)
            case 0xCA:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // INcrement X (INX)
            case 0xE8:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // Transfer A to Y (TAY)
            case 0xA8:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // Transfer Y to A (TYA)
            case 0x98:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // DEcrement Y (DEY)
            case 0x88:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // INcrement Y (INY)
            case 0xC8:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;

            // ROTATE LEFT (ROL)
            // ROL Accumulator
            case 0x2A:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // ROL Zero Page
            case 0x26:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // ROL Zero Page,X
            case 0x36:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // ROL Absolute
            case 0x2E:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // ROL Absolute,X
            case 0x3E:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;


            // ROTATE RIGHT (ROR)
            // ROR Accumulator
            case 0x6A:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // ROR Zero Page
            case 0x66:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // ROR Zero Page,X
            case 0x76:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // ROR Absolute
            case 0x6E:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // ROR Absolute,X
            case 0x7E:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;

            // RETURN FROM INTERRUPT (RTI)
            case 0x40:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;

            // RETURN FROM SUBROUTINE (RTS)
            case 0x60:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;

            // SUBTRACT WITH CARRY (SBC)
            // SBC Immediate
            case 0xE9:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // SBC Zero Page
            case 0xE5:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // SBC Zero Page,X
            case 0xF5:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // SBC Absolute
            case 0xED:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // SBC Absolute,X
            case 0xFD:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // SBC Absolute,Y
            case 0xF9:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // SBC Indirect,X
            case 0xE1:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // SBC Indirect,Y
            case 0xF1:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;

            // STORE ACCUMULATOR (STA)
            // STA Zero Page
            case 0x85:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // STA Zero Page,X
            case 0x95:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // STA Absolute
            case 0x8D:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // STA Absolute,X
            case 0x9D:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // STA Absolute,Y
            case 0x99:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // STA Indirect,X
            case 0x81:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // STA Indirect,Y
            case 0x91:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;

            // STACK INSTRUCTIONS
            // Transfer X to Stack ptr (TXS)
            case 0x9A:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // Transfer Stack ptr to X (TSX)
            case 0xBA:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // PusH Accumulator (PHA)
            case 0x48:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // PuLl Accumulator (PLA)
            case 0x68:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // PusH Processor status (PHP)
            case 0x08:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // PuLl Processor status (PLP)
            case 0x28:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;

            // STORE X REGISTER (STX)
            // STX Zero Page
            case 0x86:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // STX Zero Page,Y
            case 0x96:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // STX Absolute
            case 0x8E:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;

            // STORE Y REGISTER (STY)
            // STY Zero Page
            case 0x84:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // STY Zero Page,X
            case 0x94:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;
            // STY Absolute
            case 0x8C:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;

            default:
                System.out.println("INVALID OPCODE, NOT SUPPORTED! EXITING...");
                return -1;
        }
        return ++currentInstruction;
    }

    private void compareWithRegister(int value, int register) {
        int result = register - value;
        checkIfZero(result);
        if (value < register) {
            Status.setC((short) 0);
        } else {
            Status.setC((short) 1);
        }
        while (result < 0x00) {
            result += (0xFF + 1);
        }
        Status.setN((short) ((result >> 7) & 0x01));
    }

    private void absoluteShiftLeft(int shiftAmount, int index) {
        int shifted = memoryMap[index] << shiftAmount;
        Status.setN((short) (shifted >> 7));
        checkIfZero(shifted);
        checkIfCarry(shifted);
        memoryMap[index] = shifted & 0xFF;
    }

    private void zeroPageShiftLeft(int shiftAmount, int index) {
        int shifted = zeroPage[index] << shiftAmount;
        Status.setN((short) (shifted >> 7));
        checkIfZero(shifted);
        checkIfCarry(shifted);
        zeroPage[index] = shifted & 0xFF;
    }

    private int zeroPageAtIndex(int value) {
        return zeroPage[zeroPageIndex(value)];
    }

    private int zeroPageIndex(int value) {
        if (value > 0xff || value < 0x00) {
            System.out.println("Out of bounds Zero Page Address!");
            while (value > 0xFF) {
                value -= 0xFF;
            }
        }
        return value;
    }

    private int zeroPageAtIndexX(int value) {
        return zeroPage[zeroPageIndexX(value)];
    }

    private int zeroPageIndexX(int value) {
        int index = value + X;
        while (index > 0xFF) {
            index -= (0xFF + 1);
        }
        return index;
    }

    private int indirectX(int value) {
        int index_1 = value + X;
        if (index_1 > 0xFF) {
            index_1 -= (0xFF + 1);
        }
        int index_2 = index_1 + 1;
        if (index_2 > 0xFF) {
            index_2 -= (0xFF + 1);
        }
        return memoryMap[zeroPage[index_2] * 256 + zeroPage[index_1]];
    }

    private int indirectY(int value) {
        if (value > 0xFF)
            value -= (0xFF + 1);
        int value_2 = value + 1;
        if (value_2 > 0xFF)
            value_2 -= (0xFF + 1);
        return memoryMap[zeroPage[value_2] * 256 + zeroPage[value] + Y];
    }

    private void addToA(int value) {
        value = A + value;
        checkIfCarry(value);
        checkIfOverflow(value);
        while (value > 0xFF) {
            value -= (0xFF + 1);
        }
        while (value < 0x00) {
            value += (0xFF + 1);
        }
        checkIfZero(value);
        Status.setN((short) (value >> 7));
        A = value;
    }

    private void addToMemory(int index, int[] memory, int value) {
        value = memory[index] + value;
        while (value > 0xFF) {
            value -= (0xFF + 1);
        }
        while (value < 0x00) {
            value += (0xFF + 1);
        }
        checkIfZero(value);
        Status.setN((short) (value >> 7));
        memory[index] = value;
    }

    private void bitTestWithA(int value) {
        int m7 = (value >> 7) & 0x01;
        int m6 = (value >> 6) & 0x01;
        Status.setN((short) m7);
        Status.setN((short) m6);
        value = A & value;
        checkIfZero(value);
    }

    private void bitwiseAndWithA(int value) {
        value = A & value;
        checkIfZero(value);
        Status.setN((short) (value >> 7));
        A = value;
    }

    private void shiftALeft(int shiftAmount) {
        int value = A << shiftAmount;
        Status.setN((short) (value >> 7));
        checkIfZero(value);
        checkIfCarry(value);
        A = A & 0xFF;
    }

    private void addToX(int value) {
        value = X + value;
        while (value > 0xFF) {
            value -= (0xFF + 1);
        }
        while (value < 0x00) {
            value += (0xFF + 1);
        }
        X = value;
    }

    private void addToY(int value) {
        value = Y + value;
        while (value > 0xFF) {
            value -= (0xFF + 1);
        }
        while (value < 0x00) {
            value += (0xFF + 1);
        }
        Y = value;
    }

    private void checkIfCarry(int value) {
        if (((value >> 8) & 0x01) != 0) {
            Status.setC((short) 1);
        } else {
            Status.setC((short) 0);
        }
    }

    private void checkIfOverflow(int value) {
        if (value > 0xFF || value < 0x00) {
            Status.setV((short) 1);
        } else {
            Status.setV((short) 0);
        }
    }

    private void checkIfZero(int value) {
        value = value & 0xFF;
        if (value == 0x00) {
            Status.setZ((short) 1);
        } else {
            Status.setZ((short) 0);
        }
    }

    public int getA() {
        int value = A;
        if (value > 0x7F) {
            value -= 0x100;
        }
        return value;
    }

    public int getX() {
        int value = X;
        if (value > 0x7F) {
            value -= 0x100;
        }
        return value;
    }

    public int getY() {
        int value = Y;
        if (value > 0x7F) {
            value -= 0x100;
        }
        return value;
    }

    private StatusRegister getStatus() {
        return Status;
    }

    public void printStatus() {
        System.out.println("Carry: " + this.getStatus().getC());
        System.out.println("Overflow: " + this.getStatus().getV());
        System.out.println("Zero: " + this.getStatus().getZ());
        System.out.println("Negative: " + this.getStatus().getN());
    }
}
