import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.util.*;

// This Java program reads the XML and converts it into CSV files, adhering to the relational schema we designed. 
// The CSV files will use a tab delimiter ("\t") and end-of-line character "\n".

public class XMLToCSV {
    public static void main(String[] args) {
        try {
            // Load XML document
            // === Change the XML filepath here ===
            File inputFile = new File("ebay-data/items-0.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            // Parse and write to CSV files
            parseItems(doc);
            parseItemCategory(doc);
            parseBids(doc);
            parseBidder(doc);
            parseSeller(doc);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseItems(Document doc) throws IOException {
        NodeList nodeList = doc.getElementsByTagName("Item");
        FileWriter fw = new FileWriter("ebay-data-csv/Items.csv");
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("ItemID\tName\tCurrently\tBuy_Price\tFirst_Bid\tNumber_of_Bids\tLocation\tCountry\tStarted\tEnds\tDescription\n");
        
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

    private static void parseItemCategory(Document doc) throws IOException {
        NodeList nodeList = doc.getElementsByTagName("Item");
        FileWriter fw = new FileWriter("ebay-data-csv/ItemCategory.csv");
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("ItemID\tCategory\n");

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

    private static void parseBids(Document doc) throws IOException {
        NodeList nodeList = doc.getElementsByTagName("Bid");
        FileWriter fw = new FileWriter("ebay-data-csv/Bids.csv");
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("ItemID\tBidderID\tTime\tAmount\n");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            String bidderID = ((Element)element.getElementsByTagName("Bidder").item(0)).getAttribute("UserID");
            String time = getTagValue("Time", element);
            String amount = getTagValue("Amount", element);

            bw.write(String.format("%s\t%s\t%s\n", bidderID, time, amount));
        }
        bw.close();
    }

    private static void parseBidder(Document doc) throws IOException {
        NodeList nodeList = doc.getElementsByTagName("Bidder");
        FileWriter fw = new FileWriter("ebay-data-csv/Bidder.csv");
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("UserID\tRating\tLocation\tCountry\n");

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

    private static void parseSeller(Document doc) throws IOException {
        NodeList nodeList = doc.getElementsByTagName("Seller");
        FileWriter fw = new FileWriter("ebay-data-csv/Seller.csv");
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("UserID\tRating\n");

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
