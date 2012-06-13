/*******************************************************************************
 * Copyright (c) 2012 Nikos Papailiou.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Nikos Papailiou - initial API and implementation
 ******************************************************************************/
package gr.ntua.h2rdf.examples;

import gr.ntua.h2rdf.client.BulkLoader;
import gr.ntua.h2rdf.client.H2RDFConf;
import gr.ntua.h2rdf.client.H2RDFFactory;
import gr.ntua.h2rdf.client.Store;

import gr.ntua.h2rdf.bytes.NotSupportedDatatypeException;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;

public class BulkImportExample {
	
	public static void main(String[] args) {

		String address = "server1.org";
		String table = "DBname";
		String user = "user_name";
		H2RDFConf conf = new H2RDFConf(address, table, user);
		H2RDFFactory h2fact = new H2RDFFactory();
		Store store = h2fact.connectStore(conf);
		store.setLoader("BULK");
		
		String d0_0="http://www.Department0.University0.edu/";
		String ub ="http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#";
		String rdf ="http://www.w3.org/1999/02/22-rdf-syntax-ns#";

		BulkLoader bulkLoader = (BulkLoader) store.getLoader();
		bulkLoader.setLocalChunkSize(5000); // send data to the servers after 5000 triples

		try {
			for (int i = 0; i < 1000000; i++) {
				Node s = Node.createURI("<"+d0_0+"GraduateStudent"+i+">");
				Node p = Node.createURI("<"+ub+"takesCourse"+">");
				Node o = Node.createURI("<"+d0_0+"Course0"+">");
				Triple triple = Triple.create(s, p, o);
				store.add(triple);
				s = Node.createURI("<"+d0_0+"GraduateStudent"+i+">");
				p = Node.createURI("<"+rdf+"type"+">");
				o = Node.createURI("<"+ub+"GraduateStudent"+">");
				triple = Triple.create(s, p, o);
				store.add(triple);
			}
		} catch (NotSupportedDatatypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		bulkLoader.bulkUpdate();
		store.close();	
		
		
	}
		
}
