package com.DavidOberholzer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class RomLoader {
    private int constant[] = new int[4];
    private int prg_rom_units, chr_units, flag_6, flag_7, prg_ram_units, flag_9, flag_10;
    private static int signature[] = new int[]{0x4E, 0x45, 0x53, 0x1A};

    public RomLoader() {
    }

    public void loadRom(String filename) throws IOException {
        try {
            String file_dir = System.getProperty("user.dir") + "/" + filename;
            System.out.println("Working Directory = " + System.getProperty("user.dir") + "/" + filename);
            File f = new File(file_dir);
            FileInputStream input = new FileInputStream(f);
//            for (int i = 0; i < 4; i++) {
//                constant[i] = input.read();
//                if (constant[i] != signature[i]) {
//                    System.out.println("Invalid NES file signature! Exiting...");
//                    System.exit(-1);
//                }
//            }
//            System.out.println("Valid NES File");
//            prg_rom_units = input.read();
//            chr_units = input.read();
//            flag_6 = input.read();
//            flag_7 = input.read();
//            prg_ram_units = input.read();
//            flag_9 = input.read();
//            flag_10 = input.read();
//            for (int i = 11; i < 16; i++) {
//                int place = input.read();
//            }
            input.close();
        } finally {
            System.out.println("What!");
        }
    }
}
