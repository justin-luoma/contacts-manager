package contacts;

import util.FS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Contacts {
    private ArrayList<Contact> contacts;
    public int longestName;
    public int longestNumber;


    public Contacts() {
        this.contacts = new ArrayList<>();
        this.longestName = 0;
        this.longestNumber = 0;
    }

    public Contacts(String path) throws Exception {
        this.longestName = 0;
        this.longestNumber = 0;
        FS contactsFile = new FS(path);
        List<String> fileStrings = contactsFile.read();
        String[] s = fileStrings.get(0).split(",");
        if (s.length == 2) {
            if (!s[0].equals("name") && !s[1].equals("number")
                    && !s[0].equals("number") && !s[1].equals("name")) {
                throw new Exception("invalid file format");
            }
        } else throw new Exception("invalid file format");
        int namePos = 0;
        int numberPos = 1;
        if (fileStrings.get(0).split(",")[1].equals("name")) {
            namePos = 1;
            numberPos = 0;
        }
        fileStrings.remove(0);
        this.contacts = new ArrayList<>();
        for (String line : fileStrings) {
            String[] split = line.split(",");
            String name = split[namePos];
            long number = Long.parseLong(split[numberPos]);
            this.contacts.add(new Contact(name, number));
        }
        updatedLongest();
    }

    public ArrayList<Contact> search(String name) throws Exception {
        ArrayList<Contact> found = new ArrayList<>();
        for (Contact contact : contacts) {
            if (contact.name().toLowerCase().equals(name.toLowerCase()))
                found.add(contact);
            else if (contact.name().toLowerCase().startsWith(name.toLowerCase()))
                found.add(contact);
            else if (contact.name().toLowerCase().endsWith(name.toLowerCase()))
                found.add(contact);
        }
        if (!found.isEmpty()) {
            return found;
        }
        throw new Exception("contact does not exist");
    }

    public void addContact(Contact contact) {
        this.contacts.add(contact);
        updatedLongest();
        Collections.sort(this.contacts);
    }

    public void removeContact(Contact contact) {
        this.contacts.remove(contact);
        updatedLongest();
        Collections.sort(this.contacts);
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    protected void saveToFile(String path) throws Exception {
        ArrayList<String> lines = new ArrayList<>(this.contacts.size() + 1);
        lines.add(0, "name,number");
        for (Contact contact : this.contacts) {
            lines.add(contact.name() + "," + contact.number());
        }
        FS saveFile = new FS(path, lines);
        saveFile.write();
    }

    private void updatedLongest() {
        for (Contact contact : this.contacts) {
            String name = contact.name();
            long number = contact.number();
            if (this.longestName < name.length())
                this.longestName = name.length();
            if (this.longestNumber < (int) (Math.log10(number) + 1))
                this.longestNumber = (int) (Math.log10(number) + 1);
        }
    }
}
