package pos.machine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PosMachine {

    public int calculateTotalPrice(List<ReceiptItem> receiptItems) {
        return receiptItems.stream()
                .mapToInt(ReceiptItem::getSubTotal)
                .sum();
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

