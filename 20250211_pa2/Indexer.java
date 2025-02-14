import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.StopwordAnalyzerBase;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.sql.*;

public class Indexer {
    public static void main(String[] args) throws IOException {
        try {
            String indexPath = "indexes"; // Path where the index is stored
            rebuildIndexes(indexPath);  // Rebuild the index
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public static void insertDoc(IndexWriter i, String name, String category, String description){
		Document doc = new Document();

		// Defining fields
		doc.add(new TextField("name", name, Field.Store.YES));
		doc.add(new TextField("category", category, Field.Store.YES));
		doc.add(new TextField("description", description, Field.Store.YES));

		// Add document
		try { i.addDocument(doc); } catch (Exception e) { e.printStackTrace(); }
	}
	
	public static void rebuildIndexes(String indexPath) {
		try {
			// Directory for the index
			Path path = Paths.get(indexPath);
			System.out.println("Indexing to directory: " + indexPath);
			Directory directory = FSDirectory.open(path);

			// Define analyzer and similarity
			IndexWriterConfig config = new IndexWriterConfig(new SimpleAnalyzer());
			config.setSimilarity(new ClassicSimilarity());
			IndexWriter i = new IndexWriter(directory, config);
			
			// Clear existing index
			i.deleteAll();

			// SQL query to execute 
			String query = "SELECT i.name, GROUP_CONCAT(c.category SEPARATOR ', ') AS categories, i.description FROM Items i INNER JOIN Categories c ON i.itemId = c.itemId GROUP BY i.name, i.description;";
			System.out.println("Query: " + query);
			System.out.println("");

			// Establish the connection using the DbManager
			try (Connection conn = DbManager.getConnection(true); // Read-only connection
					Statement stmt = conn.createStatement(); // Statement to execute the query     
					ResultSet rs = stmt.executeQuery(query)) {  // Execute query

				// Process and print the results
				while (rs.next()) {
					// Get UserId and UserName attributes from the table
					String name = rs.getString("name");
					String categories = rs.getString("categories");
					String description = rs.getString("description");
 					
					// debug
					// System.out.println("name: " + name + ", categories: " + categories + ", description: " + description);
					// System.out.println("");

					// Add documents to index
					insertDoc(i, name, categories, description);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
			


			//Close writer
			i.close();
			directory.close();

			System.out.println("Indexing finished");
		} catch (Exception e) { e.printStackTrace(); }
	}
}