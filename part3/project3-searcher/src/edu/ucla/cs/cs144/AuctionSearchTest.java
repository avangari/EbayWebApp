package edu.ucla.cs.cs144;

import java.util.Calendar;
import java.util.Date;

import edu.ucla.cs.cs144.AuctionSearch;
import edu.ucla.cs.cs144.SearchRegion;
import edu.ucla.cs.cs144.SearchResult;

public class AuctionSearchTest {
	public static void main(String[] args1)
	{
		AuctionSearch as = new AuctionSearch();
		System.out.println(as.getXMLDataForItemId("1309245452"));
		
		
		// String query = "star trek";
		// int skip_results = 10;
		// int max_results = 20;
		// SearchResult[] basicResults = as.basicSearch(query, skip_results, max_results);
		// System.out.println("Skipping "+skip_results+" results and diplaying top  "+basicResults.length+" results");
		// System.out.println("Basic Seacrh Query: " + query);
		// try{
		// System.out.println("Received " + basicResults.length + " results");
		// int i=1;
		// for(SearchResult result : basicResults) {
		// 	System.out.println(i+"----->"+result.getItemId() + ": " + result.getName());
		// 	i++;
		// }
		// }
		// catch(NullPointerException e)
		// {
		// 	System.out.println("there are no matching entries for the given query");
		// }
		

		/*
		SearchRegion region =
		    new SearchRegion(33.774, -118.63, 34.201, -117.38); 
                int skip_results = 0;
		int max_results = 200000;
		SearchResult[] spatialResults = as.spatialSearch("camera", region, skip_results, max_results);
                System.out.println("Skipping "+skip_results+" results and diplaying top  "+spatialResults.length+" results");
		System.out.println("Spatial Seacrh");
		try
		{
		System.out.println("Received " + spatialResults.length + " results");
 		int i=1;
		for(SearchResult result : spatialResults) {
			System.out.println(i+"---------->"+result.getItemId() + ": " + result.getName());
			i++;		
		}
		}
		catch(NullPointerException e)
		{
			System.out.println("there are no matchin entries for the given query... the returned results are less than the numresultstoskip value");
		}
		*/

		/*String itemId = "1497595357";
		String item = as.getXMLDataForItemId(itemId);
		System.out.println("XML data for ItemId: " + itemId);
		System.out.println(item);
		*/
		// Add your own test here
	}
}

