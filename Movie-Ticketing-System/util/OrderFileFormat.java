package util;

import model.ConcessionItem;
import model.Order;
import model.Ticket;

public final class OrderFileFormat {

    private OrderFileFormat() {
    }

    public static String[] linesForAllOrders(Order[] orders, int orderCount) {
        int totalLines = 0;
        for (int i = 0; i < orderCount; i++) {
            totalLines += lineCountForOrder(orders[i]);
        }
        String[] lines = new String[totalLines];
        int write = 0;
        for (int i = 0; i < orderCount; i++) {
            String[] block = linesForSingleOrder(orders[i]);
            for (String line : block) {
                lines[write++] = line;
            }
        }
        return lines;
    }

    public static int lineCountForOrder(Order order) {
        return order.getTickets().length + order.getConcessionItems().length + 1;
    }

    public static String[] linesForSingleOrder(Order order) {
        Ticket[] tickets = order.getTickets();
        ConcessionItem[] items = order.getConcessionItems();
        String[] lines = new String[tickets.length + items.length + 1];
        int idx = 0;
        for (Ticket ticket : tickets) {
            lines[idx++] = order.getOrderId() + "|" + order.getCustomerUsername() + "|" + order.getShowtimeId()
                    + "|TICKET|" + ticket.getTicketType() + "|" + ticket.getSeatNumber() + "|" + ticket.calculatePrice();
        }
        for (ConcessionItem item : items) {
            lines[idx++] = order.getOrderId() + "|" + order.getCustomerUsername() + "|" + order.getShowtimeId()
                    + "|" + item.getCategory() + "|" + item.getName() + "|" + item.getStock() + "|" + item.getPrice();
        }
        lines[idx] = order.getOrderId() + "|" + order.getCustomerUsername() + "|" + order.getShowtimeId()
                + "|TOTAL|" + order.getTotalPrice() + "|" + order.getPaymentMethod() + "|" + order.getPurchasedAt();
        return lines;
    }
}
