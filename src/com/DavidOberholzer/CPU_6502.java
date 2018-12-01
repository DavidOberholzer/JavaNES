package com.DavidOberholzer;

public class CPU_6502 {
    private StatusRegister Status;
    private int A, X, Y, SP;
    private int zeroPage[] = new int[256];
    private int stack[] = new int[256];
    private int memoryMap[] = new int[65536];

    public CPU_6502() {
        Status = new StatusRegister();
        A = 0x00;
        X = 0x00;
        Y = 0x00;
        SP = 0xFF;
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
                this.addToA(value + Status.getC());
                break;
            // ADC Zero Page
            case 0x65:
                this.addToA(this.zeroPageAtIndex(value) + Status.getC());
                break;
            // ADC Zero Page,X
            case 0x75:
                this.addToA(this.zeroPageAtIndexX(value) + Status.getC());
                break;
            // ADC Absolute
            case 0x6D:
                this.addToA(memoryMap[value] + Status.getC());
                break;
            // ADC Absolute,X
            case 0x7D:
                this.addToA(memoryMap[value + X] + Status.getC());
                break;
            // ADC Absolute,Y
            case 0x79:
                this.addToA(memoryMap[value + Y] + Status.getC());
                break;
            // ADC Indirect,X
            case 0x61:
                this.addToA(this.indirectX(value) + Status.getC());
                break;
            // ADC Indirect,Y
            case 0x71:
                this.addToA(this.indirectY(value) + Status.getC());
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
                this.zeroPageShiftLeft(1, this.zeroPageIndexRegister(value, X));
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
                this.addToMemory(this.zeroPageIndexRegister(value, X), zeroPage, -1);
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
                bitwiseXOrWithA(value);
                break;
            // EOR Zero Page
            case 0x45:
                bitwiseXOrWithA(this.zeroPageAtIndex(value));
                break;
            // EOR Zero Page,X
            case 0x55:
                bitwiseXOrWithA(this.zeroPageAtIndexX(value));
                break;
            // EOR Absolute
            case 0x4D:
                bitwiseXOrWithA(memoryMap[value]);
                break;
            // EOR Absolute,X
            case 0x5D:
                bitwiseXOrWithA(memoryMap[value + X]);
                break;
            // EOR Absolute,Y
            case 0x59:
                bitwiseXOrWithA(memoryMap[value + Y]);
                break;
            // EOR Indirect,X
            case 0x41:
                bitwiseXOrWithA(this.indirectX(value));
                break;
            // EOR Indirect,Y
            case 0x51:
                bitwiseXOrWithA(this.indirectY(value));
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
                Status.setI((short) 0);
                break;
            // SEt Interrupt (SEI)
            case 0x78:
                Status.setI((short) 1);
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
                this.addToMemory(this.zeroPageIndex(value), zeroPage, 1);
                break;
            // INC Zero Page,X
            case 0xF6:
                this.addToMemory(this.zeroPageIndexRegister(value, X), zeroPage, 1);
                break;
            // INC Absolute
            case 0xEE:
                this.addToMemory(value, memoryMap, 1);
                break;
            // INC Absolute,X
            case 0xFE:
                this.addToMemory(value + X, memoryMap, 1);
                break;

            // JUMP (JMP)
            // JMP Absolute
            case 0x4C:
                return value;
            // JMP Indirect
            case 0x6C:
                return memoryMap[value];

            // JUMP TO SUBROUTINE (JSR)
            case 0x20:
                System.out.println("Opcode: " + opCode + " Not Implemented");
                break;

            // LOAD ACCUMULATOR (LDA)
            // LDA Immediate
            case 0xA9:
                this.loadA(value);
                break;
            // LDA Zero Page
            case 0xA5:
                this.loadA(this.zeroPageAtIndex(value));
                break;
            // LDA Zero Page,X
            case 0xB5:
                this.loadA(this.zeroPageAtIndexX(value));
                break;
            // LDA Absolute
            case 0xAD:
                this.loadA(memoryMap[value]);
                break;
            // LDA Absolute,X
            case 0xBD:
                this.loadA(memoryMap[value + X]);
                break;
            // LDA Absolute,Y
            case 0xB9:
                this.loadA(memoryMap[value + Y]);
                break;
            // LDA Indirect,X
            case 0xA1:
                this.loadA(indirectX(value));
                break;
            // LDA Indirect,Y
            case 0xB1:
                this.loadA(indirectY(value));
                break;

            // LOAD X REGISTER (LDX)
            // LDX Immediate
            case 0xA2:
                this.loadX(value);
                break;
            // LDX Zero Page
            case 0xA6:
                this.loadX(this.zeroPageAtIndex(value));
                break;
            // LDX Zero Page,Y
            case 0xB6:
                this.loadX(this.zeroPageAtIndexY(value));
                break;
            // LDX Absolute
            case 0xAE:
                this.loadX(memoryMap[value]);
                break;
            // LDX Absolute,Y
            case 0xBE:
                this.loadX(memoryMap[value + Y]);
                break;

            // LOAD Y REGISTER (LDY)
            // LDY Immediate
            case 0xA0:
                this.loadY(value);
                break;
            // LDY Zero Page
            case 0xA4:
                this.loadY(this.zeroPageAtIndex(value));
                break;
            // LDY Zero Page,X
            case 0xB4:
                this.loadY(this.zeroPageAtIndexX(value));
                break;
            // LDY Absolute
            case 0xAC:
                this.loadY(memoryMap[value]);
                break;
            // LDY Absolute,X
            case 0xBC:
                this.loadY(memoryMap[value + X]);
                break;

            // LOGICAL SHIFT RIGHT (LSR)
            // LSR Accumulator
            case 0x4A:
                A = this.logicalShiftRight(A);
                break;
            // LSR Zero Page
            case 0x46:
                this.logicalShiftRightRegister(zeroPage, this.zeroPageIndex(value));
                break;
            // LSR Zero Page,X
            case 0x56:
                this.logicalShiftRightRegister(zeroPage, this.zeroPageIndexRegister(value, X));
                break;
            // LSR Absolute
            case 0x4E:
                this.logicalShiftRightRegister(memoryMap, value);
                break;
            // LSR Absolute,X
            case 0x5E:
                this.logicalShiftRightRegister(memoryMap, value + X);
                break;

            // NO OPERATION (NOP)
            case 0xEA:
                break;

            // BITWISE OR WITH ACCUMULATOR (ORA)
            // ORA Immediate
            case 0x09:
                this.bitwiseOrWithA(value);
                break;
            // ORA Zero Page
            case 0x05:
                this.bitwiseOrWithA(this.zeroPageAtIndex(value));
                break;
            // ORA Zero Page,X
            case 0x15:
                this.bitwiseOrWithA(this.zeroPageAtIndexX(value));
                break;
            // ORA Absolute
            case 0x0D:
                this.bitwiseOrWithA(memoryMap[value]);
                break;
            // ORA Absolute,X
            case 0x1D:
                this.bitwiseOrWithA(memoryMap[value + X]);
                break;
            // ORA Absolute,Y
            case 0x19:
                this.bitwiseOrWithA(memoryMap[value + Y]);
                break;
            // ORA Indirect,X
            case 0x01:
                this.bitwiseOrWithA(this.indirectX(value));
                break;
            // ORA Indirect,Y
            case 0x11:
                this.bitwiseOrWithA(this.indirectY(value));
                break;

            // REGISTER INSTRUCTIONS
            // Transfer A to X (TAX)
            case 0xAA:
                this.transferFlags(A);
                X = A;
                break;
            // Transfer X to A (TXA)
            case 0x8A:
                this.transferFlags(X);
                A = X;
                break;
            // DEcrement X (DEX)
            case 0xCA:
                this.addToX(-1);
                break;
            // INcrement X (INX)
            case 0xE8:
                this.addToX(1);
                break;
            // Transfer A to Y (TAY)
            case 0xA8:
                this.transferFlags(A);
                Y = A;
                break;
            // Transfer Y to A (TYA)
            case 0x98:
                this.transferFlags(Y);
                A = Y;
                break;
            // DEcrement Y (DEY)
            case 0x88:
                this.addToY(-1);
                break;
            // INcrement Y (INY)
            case 0xC8:
                this.addToY(1);
                break;

            // ROTATE LEFT (ROL)
            // ROL Accumulator
            case 0x2A:
                A = this.logicalShiftLeft(A);
                break;
            // ROL Zero Page
            case 0x26:
                this.logicalShiftLeftRegister(zeroPage, this.zeroPageIndex(value));
                break;
            // ROL Zero Page,X
            case 0x36:
                this.logicalShiftLeftRegister(zeroPage, this.zeroPageIndexRegister(value, X));
                break;
            // ROL Absolute
            case 0x2E:
                this.logicalShiftLeftRegister(memoryMap, value);
                break;
            // ROL Absolute,X
            case 0x3E:
                this.logicalShiftLeftRegister(memoryMap, value + X);
                break;


            // ROTATE RIGHT (ROR)
            // ROR Accumulator
            case 0x6A:
                A = this.logicalShiftRight(value);
                break;
            // ROR Zero Page
            case 0x66:
                this.logicalShiftRightRegister(zeroPage, this.zeroPageIndex(value));
                break;
            // ROR Zero Page,X
            case 0x76:
                this.logicalShiftRightRegister(zeroPage, this.zeroPageIndexRegister(value, X));
                break;
            // ROR Absolute
            case 0x6E:
                this.logicalShiftRightRegister(memoryMap, value);
                break;
            // ROR Absolute,X
            case 0x7E:
                this.logicalShiftRightRegister(memoryMap, value + X);
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
                this.addToA(-value - Status.getC());
                break;
            // SBC Zero Page
            case 0xE5:
                this.addToA(-this.zeroPageAtIndex(value) - Status.getC());
                break;
            // SBC Zero Page,X
            case 0xF5:
                this.addToA(-this.zeroPageAtIndexX(value) - Status.getC());
                break;
            // SBC Absolute
            case 0xED:
                this.addToA(-memoryMap[value] - Status.getC());
                break;
            // SBC Absolute,X
            case 0xFD:
                this.addToA(-memoryMap[value + X] - Status.getC());
                break;
            // SBC Absolute,Y
            case 0xF9:
                this.addToA(-memoryMap[value + Y] - Status.getC());
                break;
            // SBC Indirect,X
            case 0xE1:
                this.addToA(-this.indirectX(value) - Status.getC());
                break;
            // SBC Indirect,Y
            case 0xF1:
                this.addToA(-this.indirectY(value) - Status.getC());
                break;

            // STORE ACCUMULATOR (STA)
            // STA Zero Page
            case 0x85:
                zeroPage[this.zeroPageIndex(value)] = A;
                break;
            // STA Zero Page,X
            case 0x95:
                zeroPage[this.zeroPageIndexRegister(value, X)] = A;
                break;
            // STA Absolute
            case 0x8D:
                memoryMap[value] = A;
                break;
            // STA Absolute,X
            case 0x9D:
                memoryMap[value + X] = A;
                break;
            // STA Absolute,Y
            case 0x99:
                memoryMap[value + Y] = A;
                break;
            // STA Indirect,X
            case 0x81:
                memoryMap[this.indirectXIndex(value)] = A;
                break;
            // STA Indirect,Y
            case 0x91:
                memoryMap[this.indirectYIndex(value)] = A;
                break;

            // STACK INSTRUCTIONS
            // Transfer X to Stack ptr (TXS)
            case 0x9A:
                SP = X;
                break;
            // Transfer Stack ptr to X (TSX)
            case 0xBA:
                this.transferFlags(SP);
                X = SP;
                break;
            // PusH Accumulator (PHA)
            case 0x48:
                stack[SP] = A;
                this.addToSP(-1);
                break;
            // PuLl Accumulator (PLA)
            case 0x68:
                this.transferFlags(stack[SP]);
                A = stack[SP];
                this.addToSP(1);
                break;
            // PusH Processor status (PHP)
            case 0x08:
                stack[SP] = Status.getStatusByte();
                this.addToSP(-1);
                break;
            // PuLl Processor status (PLP)
            case 0x28:
                Status.loadStatusByte(stack[SP]);
                this.addToSP(1);
                break;

            // STORE X REGISTER (STX)
            // STX Zero Page
            case 0x86:
                zeroPage[this.zeroPageIndex(value)] = X;
                break;
            // STX Zero Page,Y
            case 0x96:
                zeroPage[this.zeroPageIndexRegister(value, Y)] = X;
                break;
            // STX Absolute
            case 0x8E:
                memoryMap[value] = X;
                break;

            // STORE Y REGISTER (STY)
            // STY Zero Page
            case 0x84:
                zeroPage[this.zeroPageIndex(value)] = Y;
                break;
            // STY Zero Page,X
            case 0x94:
                zeroPage[this.zeroPageIndexRegister(value, X)] = Y;
                break;
            // STY Absolute
            case 0x8C:
                memoryMap[value] = Y;
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
        return zeroPage[zeroPageIndexRegister(value, X)];
    }

    private int zeroPageAtIndexY(int value) {
        return zeroPage[zeroPageIndexRegister(value, Y)];
    }

    private int zeroPageIndexRegister(int value, int m_index) {
        int index = value + m_index;
        while (index > 0xFF) {
            index -= (0xFF + 1);
        }
        return index;
    }

    private int indirectXIndex(int value) {
        int index_1 = value + X;
        if (index_1 > 0xFF) {
            index_1 -= (0xFF + 1);
        }
        int index_2 = index_1 + 1;
        if (index_2 > 0xFF) {
            index_2 -= (0xFF + 1);
        }
        return zeroPage[index_2] * 256 + zeroPage[index_1];
    }

    private int indirectX(int value) {
        return memoryMap[this.indirectXIndex(value)];
    }

    private int indirectYIndex(int value) {
        if (value > 0xFF)
            value -= (0xFF + 1);
        int value_2 = value + 1;
        if (value_2 > 0xFF)
            value_2 -= (0xFF + 1);
        return zeroPage[value_2] * 256 + zeroPage[value] + Y;
    }

    private int indirectY(int value) {
        return memoryMap[this.indirectYIndex(value)];
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
        checkIfNegative(value);
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
        checkIfNegative(value);
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

    private void bitwiseOrWithA(int value) {
        value = A | value;
        checkIfZero(value);
        checkIfNegative(value);
        A = value;
    }

    private void bitwiseXOrWithA(int value) {
        value = A ^ value;
        checkIfZero(value);
        checkIfNegative(value);
        A = value;
    }

    private void bitwiseAndWithA(int value) {
        value = A & value;
        checkIfZero(value);
        checkIfNegative(value);
        A = value;
    }

    private void shiftALeft(int shiftAmount) {
        int value = A << shiftAmount;
        checkIfNegative(value);
        checkIfZero(value);
        checkIfCarry(value);
        A = A & 0xFF;
    }

    private int logicalShiftLeft(int value) {
        int bit_7 = (value & 0x80) >> 7;
        Status.setC((short) bit_7);
        value = (value << 1) | (bit_7);
        checkIfZero(value);
        checkIfNegative(value);
        return value;
    }

    private void logicalShiftLeftRegister(int[] register, int index) {
        register[index] = this.logicalShiftLeft(register[index]);
    }

    private int logicalShiftRight(int value) {
        int bit_0 = value & 0x01;
        Status.setC((short) bit_0);
        value = (value >> 1) | (bit_0 << 7);
        checkIfZero(value);
        checkIfNegative(value);
        return value;
    }

    private void logicalShiftRightRegister(int[] register, int index) {
        register[index] = this.logicalShiftRight(register[index]);
    }

    private void loadA(int value) {
        checkIfZero(value);
        checkIfNegative(value);
        A = value;
    }

    private void loadX(int value) {
        checkIfZero(value);
        checkIfNegative(value);
        X = value;
    }

    private void loadY(int value) {
        checkIfZero(value);
        checkIfNegative(value);
        Y = value;
    }

    private void addToX(int value) {
        value = X + value;
        while (value > 0xFF) {
            value -= (0xFF + 1);
        }
        while (value < 0x00) {
            value += (0xFF + 1);
        }
        checkIfZero(value);
        checkIfNegative(value);
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
        checkIfZero(value);
        checkIfNegative(value);
        Y = value;
    }

    private void addToSP(int value) {
        value = Y + value;
        if (value > 0xFF) {
            value = 0xff;
        }
        if (value < 0x00) {
            System.out.println("Stack overflow!");
            System.exit(-1);
        }
        SP = value;
    }

    private void transferFlags(int value) {
        checkIfZero(value);
        checkIfNegative(value);
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

    private void checkIfNegative(int value) {
        value = 0x01 & (value >> 7);
        Status.setN((short) value);
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
