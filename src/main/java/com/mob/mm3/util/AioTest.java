package com.mob.mm3.util;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AioTest {

    public static void main(String[] args) throws Exception {
//        System.out.println(readFileAio("http://api.fs.internal.mob.com/fs/download?module=dataexchange&path=pine_cone/20210202/20210202_wkb_xxl_1_click_wkb_bx_cucc_new.tar.gz"));

        List l1 = Arrays.asList("1","2");
//        test1(l1);
        System.out.println(l1);

        System.out.println("123.ext".contains("."));
    }

    private static void test1(List<String> l1) {
        l1.clear();
    }


    public static String readFileAio(String fileName) throws IOException, ExecutionException, InterruptedException {
        try (AsynchronousFileChannel channel = AsynchronousFileChannel.open(Paths.get(fileName), StandardOpenOption.READ)) {
            StringBuilder sb = new StringBuilder();
            ByteBuffer buffer = ByteBuffer.allocate(32);
            Charset charset = Charset.forName("utf-8");
            int pos = 0;
            while (pos < channel.size()) {
                Future<Integer> read = channel.read(buffer, pos);
                while (!read.isDone()) ;
                pos += read.get();
                buffer.flip();
                sb.append(charset.decode(buffer).toString());
                buffer.compact();
            }
            return sb.toString();
        }
    }
}
