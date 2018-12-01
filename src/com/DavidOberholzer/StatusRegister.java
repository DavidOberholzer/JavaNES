package com.DavidOberholzer;

public class StatusRegister {
    private short N = 0, V = 0, B = 0, D = 0, I = 0, Z = 0, C = 0;

    public StatusRegister() {
    }

    public int getStatusByte() {
        return ((this.getN() << 7) | (this.getV() << 6) | (this.getB() << 4) | (this.getD() << 3) | (this.getI() << 2) | (this.getZ() << 1) | this.getC());
    }

    public void loadStatusByte(int value) {
        this.setN((short) ((value >> 7) & 0x01));
        this.setV((short) ((value >> 6) & 0x01));
        this.setB((short) ((value >> 4) & 0x01));
        this.setD((short) ((value >> 3) & 0x01));
        this.setI((short) ((value >> 2) & 0x01));
        this.setZ((short) ((value >> 1) & 0x01));
        this.setC((short) (value & 0x01));
    }

    public short getN() {
        return N;
    }

    public void setN(short n) {
        N = n;
    }

    public short getV() {
        return V;
    }

    public void setV(short v) {
        V = v;
    }

    public short getB() {
        return B;
    }

    public void setB(short b) {
        B = b;
    }

    public short getD() {
        return D;
    }

    public void setD(short d) {
        D = d;
    }

    public short getI() {
        return I;
    }

    public void setI(short i) {
        I = i;
    }

    public short getZ() {
        return Z;
    }

    public void setZ(short z) {
        Z = z;
    }

    public short getC() {
        return C;
    }

    public void setC(short c) {
        C = c;
    }
}
