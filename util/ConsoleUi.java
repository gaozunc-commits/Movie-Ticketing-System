package util;

/**
 * Shared console banners and rules for consistent menus and receipts.
 */
public final class ConsoleUi {

    private static final int DEFAULT_WIDTH = 52;

    private ConsoleUi() {
    }

    public static void banner(String title) {
        rule();
        System.out.println("  " + title);
        rule();
    }

    public static void rule() {
        System.out.println("=".repeat(DEFAULT_WIDTH));
    }

    public static void lightRule() {
        System.out.println("-".repeat(DEFAULT_WIDTH));
    }
}
