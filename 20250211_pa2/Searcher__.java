import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.search.similarities.ClassicSimilarity;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;

public class Searcher {
    private IndexSearcher searcher;
    private MultiFieldQueryParser parser;

    public Searcher(String indexDir) throws IOException {
        // Init index reader
        Directory directory = FSDirectory.open(Paths.get(indexDir));
        IndexReader indexReader = DirectoryReader.open(directory);
        searcher = new IndexSearcher(indexReader);
        
        String[] fields = {"name", "categories", "description"};
        parser = new MultiFieldQueryParser(fields, new SimpleAnalyzer());
    }

    public void search(String queryStr, int numResults) throws IOException, ParseException {
        Query query = parser.parse(queryStr);
        TopDocs topDocs = searcher.search(query, numResults);

        System.out.println("# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -#");
        System.out.println("Number of hits: " + topDocs.totalHits.value());
        System.out.println("# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -#");

        try (FileWriter writer = new FileWriter("results.html")) {
            writer.write("<html><body>\n");
            writer.write("<h1>Search Results</h1>\n");
            writer.write("<p>Number of hits: " + topDocs.totalHits.value() + "</p>\n");
            writer.write("<table border='1'>\n");
            writer.write("<tr><th>ItemId</th><th>Name</th><th>Score</th></tr>\n");

            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document doc = searcher.doc(scoreDoc.doc);
                String itemId = doc.get("itemId");
                String name = doc.get("name");
                float score = scoreDoc.score;

                System.out.println("ItemId: " + itemId + ", Name: " + name + ", Score: " + score);
                writer.write("<tr><td>" + itemId + "</td><td>" + name + "</td><td>" + score + "</td></tr>\n");
            }
            writer.write("</table>\n");
            writer.write("</body></html>");
        }
        System.out.println("Results stored in results.html");
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java Searcher \"<keywords>\" <numResults>");
            return;
        }

        String queryStr = args[0];
        int numResults = Integer.parseInt(args[1]);
        String indexDir = "indexes";

        try {
            Searcher searcher = new Searcher(indexDir);
            searcher.search(queryStr, numResults);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
