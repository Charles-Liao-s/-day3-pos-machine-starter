package pos.machine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PosMachine {
    public String printReceipt(List<String> barcodes) {
        Receipt receipt = calculateCost(barcodes);
        return renderReceipt(receipt);
    }
    public String renderReceipt(Receipt receipt) {
        return generateReceipt(receipt);
    }
    public String generateReceipt(Receipt receipt) {
        StringBuilder sb = new StringBuilder();
        sb.append("***<store earning no money>Receipt***\n");

        for (ReceiptItem item : receipt.getReceiptItems()) {
            sb.append(generateItemsReceipt(item)).append("\n");
        }

        sb.append("----------------------\n");
        sb.append(String.format("Total: %d (yuan)\n", receipt.getTotalPrice()));
        sb.append("**********************");

        return sb.toString();
    }
    public String generateItemsReceipt(ReceiptItem item) {
        return String.format("Name: %s, Quantity: %d, Unit price: %d (yuan), Subtotal: %d (yuan)",
                item.getName(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getSubTotal());
    }
    public int calculateTotalPrice(List<ReceiptItem> receiptItems) {
        return receiptItems.stream()
                .mapToInt(ReceiptItem::getSubTotal)
                .sum();
    }
    public Receipt calculateCost(List<String> barcodes) {
        List<ReceiptItem> receiptItems = decodeToItems(barcodes);
        int totalPrice = calculateTotalPrice(receiptItems);
        return new Receipt(receiptItems, totalPrice);
    }
    public List<ReceiptItem> decodeToItems(List<String> barcodes) {
        List<Item> allItems = ItemsLoader.loadAllItems();

        Map<String, Item> itemMap = allItems.stream()
                .collect(HashMap::new,
                        (map, item) -> map.put(item.getBarcode(), item),
                        HashMap::putAll);

        Map<String, Integer> countMap = new HashMap<>();

        for (String barcode : barcodes) {
            countMap.merge(barcode, 1, Integer::sum);
        }

        List<ReceiptItem> receiptItems = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
            String barcode = entry.getKey();
            int quantity = entry.getValue();
            Item item = itemMap.get(barcode);
            if (item != null) {
                receiptItems.add(new ReceiptItem(item.getName(), quantity, item.getPrice()));
            }
        }

        return receiptItems;
    }
}

