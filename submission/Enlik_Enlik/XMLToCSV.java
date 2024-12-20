import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.util.*;

// This Java program reads the XML and converts it into CSV files, adhering to the relational schema we designed. 
// The CSV files will use a tab delimiter ("\t") and end-of-line character "\n".
// It's using DOM parsing

public class XMLToCSV {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Please provide the input XML file path and the output directory path as command line arguments.");
            return;
        }
        
        String inputFilePath = args[0];
        String outputDirPath = args[1];
        
        try {
            System.out.println("Starting XML to CSV conversion...");
            File inputFile = new File(inputFilePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            parseItems(doc, outputDirPath);
            System.out.println("Items.csv created.");

            parseItemCategory(doc, outputDirPath);
            System.out.println("ItemCategory.csv created.");

            parseBids(doc, outputDirPath);
            System.out.println("Bids.csv created.");

            parseBidder(doc, outputDirPath);
            System.out.println("Bidder.csv created.");

            parseSeller(doc, outputDirPath);
            System.out.println("Seller.csv created.");

            System.out.println("XML to CSV conversion completed successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred during the XML to CSV conversion.");
        }
    }

    private static void parseItems(Document doc, String outputDirPath) throws IOException {
        NodeList nodeList = doc.getElementsByTagName("Item");
        File file = new File(outputDirPath + "/Items.csv");
        boolean isNewFile = !file.exists();
        FileWriter fw = new FileWriter(file, true);
        BufferedWriter bw = new BufferedWriter(fw);
        
        if (isNewFile) {
            bw.write("ItemID\tName\tCurrently\tBuy_Price\tFirst_Bid\tNumber_of_Bids\tLocation\tCountry\tStarted\tEnds\tDescription\n");
        }

        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            String itemID = element.getAttribute("ItemID");
            String name = getTagValue("Name", element);
            String currently = getTagValue("Currently", element);
            String buyPrice = getTagValue("Buy_Price", element);
            String firstBid = getTagValue("First_Bid", element);
            String numberOfBids = getTagValue("Number_of_Bids", element);
            String location = getTagValue("Location", element);
            String country = getTagValue("Country", element);
            String started = getTagValue("Started", element);
            String ends = getTagValue("Ends", element);
            String description = getTagValue("Description", element);
            
            bw.write(String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n",
                    itemID, name, currently, buyPrice, firstBid, numberOfBids, location, country, started, ends, description));
        }
        bw.close();
    }

    private static void parseItemCategory(Document doc, String outputDirPath) throws IOException {
        NodeList nodeList = doc.getElementsByTagName("Item");
        File file = new File(outputDirPath + "/ItemCategory.csv");
        boolean isNewFile = !file.exists();
        FileWriter fw = new FileWriter(file, true);
        BufferedWriter bw = new BufferedWriter(fw);
        
        if (isNewFile) {
            bw.write("ItemID\tCategory\n");
        }

        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            String itemID = element.getAttribute("ItemID");
            NodeList categories = element.getElementsByTagName("Category");
            for (int j = 0; j < categories.getLength(); j++) {
                String category = categories.item(j).getTextContent();
                bw.write(String.format("%s\t%s\n", itemID, category));
            }
        }
        bw.close();
    }

    private static void parseBids(Document doc, String outputDirPath) throws IOException {
        NodeList nodeList = doc.getElementsByTagName("Bid");
        File file = new File(outputDirPath + "/Bids.csv");
        boolean isNewFile = !file.exists();
        FileWriter fw = new FileWriter(file, true);
        BufferedWriter bw = new BufferedWriter(fw);
        
        if (isNewFile) {
            bw.write("BidderID\tTime\tAmount\n");
        }

        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            String bidderID = ((Element)element.getElementsByTagName("Bidder").item(0)).getAttribute("UserID");
            String time = getTagValue("Time", element);
            String amount = getTagValue("Amount", element);

            bw.write(String.format("%s\t%s\t%s\n", bidderID, time, amount));
        }
        bw.close();
    }

    private static void parseBidder(Document doc, String outputDirPath) throws IOException {
        NodeList nodeList = doc.getElementsByTagName("Bidder");
        File file = new File(outputDirPath + "/Bidder.csv");
        boolean isNewFile = !file.exists();
        FileWriter fw = new FileWriter(file, true);
        BufferedWriter bw = new BufferedWriter(fw);
        
        if (isNewFile) {
            bw.write("UserID\tRating\tLocation\tCountry\n");
        }

        Set<String> bidders = new HashSet<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            String userID = element.getAttribute("UserID");

            if (!bidders.contains(userID)) {
                bidders.add(userID);
                String rating = element.getAttribute("Rating");
                String location = getTagValue("Location", element);
                String country = getTagValue("Country", element);

                bw.write(String.format("%s\t%s\t%s\t%s\n", userID, rating, location, country));
            }
        }
        bw.close();
    }

    private static void parseSeller(Document doc, String outputDirPath) throws IOException {
        NodeList nodeList = doc.getElementsByTagName("Seller");
        File file = new File(outputDirPath + "/Seller.csv");
        boolean isNewFile = !file.exists();
        FileWriter fw = new FileWriter(file, true);
        BufferedWriter bw = new BufferedWriter(fw);
        
        if (isNewFile) {
            bw.write("UserID\tRating\n");
        }

        Set<String> sellers = new HashSet<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            String userID = element.getAttribute("UserID");

            if (!sellers.contains(userID)) {
                sellers.add(userID);
                String rating = element.getAttribute("Rating");
                bw.write(String.format("%s\t%s\n", userID, rating));
            }
        }
        bw.close();
    }

    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return "";
    }
}
