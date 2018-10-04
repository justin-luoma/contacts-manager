package contacts;

import util.Input;

import java.util.ArrayList;

public class ContactsApplication {
    private static Contacts contacts;
    private static Input input = new Input();

    public static void main(String[] args) {
        try {
            contacts = new Contacts("data/contacts.txt");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        boolean stop = false;
        do {
            System.out.println("1. View contacts.");
            System.out.println("2. Add a new contact.");
            System.out.println("3. Search a contact by name.");
            System.out.println("4. Delete an existing contact");
            System.out.println("5. Exit.");
            System.out.print("\nEnter an option: ");
            switch (input.getInt(1, 5)) {
                case 1:
                    view();
                    break;
                case 2:
                    add();
                    break;
                case 3:
                    search();
                    break;
                case 4:
                    delete();
                    break;
                case 5:
                    stop = true;
                    break;
                default:
                    break;
            }
        } while (!stop);
        try {
            contacts.saveToFile("data/contacts.txt");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void view() {
        if (contacts.getContacts().size() == 0) {
            System.out.println("\nYour contact list is currently empty.\n");
            return;
        }
        String header = String.format(
                "| %-" + (contacts.longestName + 1) +
                        "s| %-" + (contacts.longestNumber + 3) + "s|", "Name"
                , "Number");
        System.out.printf("%n%s%n", header);
        System.out.print("|");
        System.out.print(new String(new char[contacts.longestName + 2])
                .replace("\0", "-"));
        System.out.print("|");
        System.out.print(new String(new char[contacts.longestNumber + 4])
                .replace("\0", "-"));
        System.out.print("|\n");
        for (Contact contact : contacts.getContacts()) {
            System.out.printf("| %-" + (contacts.longestName + 1) + "s| %-"
                            + (contacts.longestNumber + 3) + "s|%n",
                    contact.name(), formatNumber(contact.number()));
        }
        System.out.println();
    }

    private static void add() {
        System.out.println("\nNew Contact:");
        String name = input.getString("Name: ");
        long number = input.getLong("Number: ");
        while (((int) (Math.log10(number) + 1)) != 7 && ((int) (Math.log10(number) + 1) != 10)) {
            System.out.println("\ninvalid phone number: 1234567 or 1234567890");
            number = input.getLong("Number: ");
        }
        System.out.println();
        try {
            ArrayList<Contact> match =  contacts.search(name);
            switch (match.size()) {
                case 1:
                    if (input.yesNo("There's already a contact named " + name + ". Do" +
                            " you want to overwrite it? (Yes/No): ")) {
                        contacts.removeContact(match.get(0));
                        contacts.addContact(new Contact(name, number));
                    }
                    System.out.println();
                    break;
                default:
                    System.out.println("There were multiple matches to the contact you're trying to add.");
                    System.out.println();
                    break;
            }

        } catch (Exception e) {
            contacts.addContact(new Contact(name, number));
        }
    }

    private static void search() {
        String searchString = input.getString("\nName to search: ");
        try {
            ArrayList<Contact> results = contacts.search(searchString);
            int nameL = 0;
            int numberL = 0;
            for (Contact contact : results) {
                String name = contact.name();
                long number = contact.number();
                if (nameL < name.length())
                    nameL = name.length();
                if (numberL < (int) (Math.log10(number) + 1))
                    numberL = (int) (Math.log10(number) + 1);
            }
            String header = String.format(
                    "| %-" + (nameL + 1) +
                            "s| %-" + (numberL + 1) + "s|", "Name", "Number");
            System.out.printf("%n%s%n", header);
            System.out.print("|");
            System.out.print(new String(new char[nameL + 2])
                    .replace("\0", "-"));
            System.out.print("|");
            System.out.print(new String(new char[numberL + 2])
                    .replace("\0", "-"));
            System.out.print("|\n");
            for (Contact contact : results) {
                System.out.printf("| %-" + (nameL + 1) + "s| %-"
                                + (numberL + 1) + "d|%n",
                        contact.name(), contact.number());
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println();
        }
    }

    private static void delete() {
        System.out.println();
        ArrayList<Contact> contactL = contacts.getContacts();
        for (int i = 0; i < contactL.size(); i++) {
            System.out.printf("%d: %s%n", i + 1, contactL.get(i).name());
        }
        int contactN = input.getInt(1, contactL.size(), "Which contact would " +
                "you like to delete? ");
        contacts.removeContact(contactL.get(contactN - 1));
        System.out.println();
    }

    private static String formatNumber(long number) {
        String numStr = String.valueOf(number);
        switch (numStr.length()) {
            case 7:
                return numStr.substring(0, 3) + "-" + numStr.substring(3, 7);
            case 10:
                return numStr.substring(0, 3) + "-" + numStr.substring(3, 6) + "-" + numStr.substring(6, 10);
        }
        return numStr;
    }
}
