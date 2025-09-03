package pos.machine;

import java.util.Arrays;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        PosMachine machine = new PosMachine();
        List<String> barcodes = Arrays.asList(
                "ITEM000000",
                "ITEM000000",
                "ITEM000000",
                "ITEM000000",
                "ITEM000001",
                "ITEM000001",
                "ITEM000004",
                "ITEM000004",
                "ITEM000004"
        );
        System.out.println(machine.printReceipt(barcodes));
    }
}
