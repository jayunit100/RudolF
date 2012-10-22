package dev;
import java.io.File;
import java.net.UnknownHostException;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.WriteConcern;

import dev.derbyutils.DerbyUtils;
import dev.derbyutils.DerbyWorkerThread;

/**
 * A resource intensive process to test JVM
 * 
 * Mongo is a fast data source to generate test data
 * Apache derby is a pure java embedded object that will be main targert of profiling, as hopefully it will 
 * consume lots of memory on the heap
 * 
 * The java mongo boiler plate  loop sets up the worker thread that takes the mongo jason and maps to SQL 
 * this loop will also be a bottle neck to be investigated for the stack memory and loop optimisation
 * 
 * By making the  java mongo boiler plate loop spawn a thread for each JDBC  derby update I can generate an out 
 * of memory error in a controlled fashion by setting the number of loops, andor changing the data through put for each 
 * iteration
 */
public class JvmResearch   {
	
	private static Mongo m;
	private static String sep = File.separator;
	private static int k ;;
	
	public static void main(String[] args) throws Exception {

		try {
			m = new Mongo("localhost", 27017);
		} 
		catch (UnknownHostException e) {
			e.printStackTrace();
		}

		// this is just a database I have on my system with a lot of records for
		// a data source for testing purposes
		DB db = m.getDB("collector-actionfigures-mainpage");
		DBCollection fileredColl = db
				.getCollection("Terminator_Brand_Filtered");
		m.setWriteConcern(WriteConcern.SAFE);

		DerbyUtils.makeDerby("e:dbDerb" + sep + "firstTest");

		BasicDBObject query = new BasicDBObject();
		query.put("id", new BasicDBObject("$gt", 100));
		DBCursor cursor = fileredColl.find();

		/*
		 * this will be a resource intensive loop for investigation by profiling
		 * tools will look at hot code optimization here , this loop spawns
		 * threads : so each derby update will be in unique thread, hopefully
		 * this will be where i can generate a "out of mem error" in a
		 * controllable repeatable testable process at the moment I can generate
		 * 100 threads with no error on my 4 Gb memory intel box
		 */

		k = 1;
		try {
			while (cursor.hasNext()) {
				DBObject doc = cursor.next();
				DerbyWorkerThread worker = new DerbyWorkerThread(doc);
				// pause for a while as derby is slow
				try {
					Thread.sleep(1600);
				} catch (InterruptedException e) {
				}
				;
				System.out.println("insert: " + (k++) + ", completed");

			}// end while

		} finally {
			cursor.close();
			// all done drop the database as is just junk test data
			System.out.println("dropDerby");
			DerbyUtils.dropDerby("e:dbDerb" + sep + "firstTest");
		}

	}
}
