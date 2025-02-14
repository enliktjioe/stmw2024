import java.io.*;
import java.nio.file.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;

import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.*;
import org.apache.lucene.document.*;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.StoredFields;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Searcher {
    public Searcher() {
    }

    public static void main(String[] args) throws Exception {
        try {
            // Path where the index is stored
            String indexPath = "indexes";
            if (args.length > 0) {
                // First argument is the search query
                String searchQuery = args[0];
                // Second argument is the number of results to show
                int numResults = Integer.parseInt(args[1]);

                // Optional geo-location parameters
                double longitude = 0, latitude = 0, width = 0;
                boolean geoSearch = false;
                for (int i = 2; i < args.length; i++) {
                    switch (args[i]) {
                        case "-x":
                            longitude = Double.parseDouble(args[++i]);
                            geoSearch = true;
                            break;
                        case "-y":
                            latitude = Double.parseDouble(args[++i]);
                            geoSearch = true;
                            break;
                        case "-w":
                            width = Double.parseDouble(args[++i]);
                            geoSearch = true;
                            break;
                    }
                }

                System.out.println("Searching for: " + searchQuery);
                if (geoSearch) {
                    System.out.println("Geo-location search enabled: longitude=" + longitude + ", latitude=" + latitude + ", width=" + width + " km");
                }

                // Search index in the specified fields for the query and return a defined maximum number of results
                search(indexPath, searchQuery, numResults, geoSearch, longitude, latitude, width);
            } else {
                System.out.println("Usage: java Searcher <searchQuery> <numResults> [-x longitude] [-y latitude] [-w width]");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void search(String indexPath, String searchText, int numResults, boolean geoSearch, double longitude, double latitude, double width) {
        try {
            // Init index reader
            Path path = Paths.get(indexPath);
            Directory directory = FSDirectory.open(path);
            IndexReader indexReader = DirectoryReader.open(directory);
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);
            indexSearcher.setSimilarity(new ClassicSimilarity());

            // Define the fields to search
            String[] fields = {"name", "categories", "description"};
            SimpleAnalyzer analyzer = new SimpleAnalyzer();
            MultiFieldQueryParser queryParser = new MultiFieldQueryParser(fields, analyzer);
            Query query = queryParser.parse(searchText);

            // Search index (get top results)
            TopDocs topDocs = indexSearcher.search(query, numResults);
            System.out.println();
            System.out.println("#----------------------------------------#");
            System.out.println("Number of hits: " + topDocs.totalHits.value());
            System.out.println("#----------------------------------------#");

            // Iterate over results and print them
            StoredFields storedFields = indexSearcher.storedFields();
            for (ScoreDoc hit : topDocs.scoreDocs) {
                Document doc = storedFields.document(hit.doc); // Get document from hit
                Explanation explanation = indexSearcher.explain(query, hit.doc);

                // If geo-location search is enabled, filter results based on distance
                if (geoSearch) {
                    // double docLongitude = Double.parseDouble(doc.get("longitude"));
                    // double docLatitude = Double.parseDouble(doc.get("latitude"));
                    String docLongitudeStr = doc.get("longitude");
                    String docLatitudeStr = doc.get("latitude");
                    double docLongitude = Double.parseDouble(docLongitudeStr);
                    double docLatitude = Double.parseDouble(docLatitudeStr);
                    double distance = haversineDistance(latitude, longitude, docLatitude, docLongitude);
                    // double distance = vincentyDistance(latitude, longitude, docLatitude, docLongitude);
                    
                    System.out.println("itemId: " + doc.get("itemId") + ", name: " + doc.get("name") + ", score: " + hit.score + ", distance: " + distance + " km" + ", price: " + doc.get("price"));
                    // System.out.println("Explanation: " + explanation.toString());
                } else {
                    System.out.println("itemId: " + doc.get("itemId") + ", name: " + doc.get("name") + ", score: " + hit.score + ", price: " + doc.get("price"));
                    // System.out.println("Explanation: " + explanation.toString());
                }
            }
            indexReader.close();
            directory.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Haversine formula to calculate the distance between two geo-locations
    public static double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c; // Distance in km
        return Math.round(distance * 100.0) / 100.0; // Round to two decimal places
    }
}
