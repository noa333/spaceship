
package chapter1;


public class Chapter1 {


    public static void main(String[] args) {
        byte small = (byte)3;
        System.out.println(small);
        small = (byte)127;
        System.out.println(small);
        small = (byte)128;
        System.out.println(small);
        small = (byte)129;
        System.out.println(small);
        small = (byte)256;
        System.out.println(small);
        
        byte bobby = (byte)0xb7;
        
        int number = bobby & 0x08;
        if (number == 0)
            System.out.println("no bus");
        else
            System.out.println("yes bus");
        
        
        
        number = bobby & 0x01;
        if (number == 0)
            System.out.println("male");
        else
            System.out.println("female");
        
        
        
        number = bobby & 0x80;
        if (number == 0)
            System.out.println("cafe");
        else
            System.out.println("home lunch");
        
        
        
        number = bobby & 0x00;
        if (number == 0)
            System.out.println("no sports");
        else
            System.out.println("sports");
        
        
        
        double value = .1 * 8;
        double count = .1 + .1 + .1 + .1 + .1 + .1 + .1 + .1;
        System.out.println(value);
        System.out.println(count);
        if (value == count)
            System.out.println("values are the same");
        else
            System.out.println("values not the same");
        
        
        final double DELTA = .00000001;
        if (value + DELTA > count && value - DELTA < count)
            System.out.println("values are the same");
        else
            System.out.println("values not the same");
        
        
        int val = 2;
        int num = val++;
        System.out.println("num = " + num);
        
        val = 2;
        num = ++val;
        System.out.println("num = " + num);
        
        System.out.print("and\\or \\n\n");
        
    }
}
