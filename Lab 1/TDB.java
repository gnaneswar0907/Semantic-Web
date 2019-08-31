// TDB provides Serializable Transactions via a Dataset Model
// CHANGES:
//    Add dataset to TDB directory
//        Dataset dataset=TDBFactory.createDataset("someDatasetDir");
//    In-memory model to dataset model
//        modelMem = ModelFactory.getDefaultModel()
//      versus
//        modelTDB = dataset.getNamedModel("name"); // default model also exists
//
// READ via:
//    SPARQL queries (SELECT) on dataset
//        QueryExecutionFactory.create(strQuery, dataset);
//    getting model from dataset
//        modelTDB = dataset.get...()
//    copy dataset model to in-memory model
//        modelMem.add( modelTDB );
//
// WRITE via
//    SPARQL updates on graphstore of dataset
//        GraphStore graphStore = GraphStoreFactory.create(dataset);
//        UpdateRequest request = UpdateFactory.create(strUpdate);
//        UpdateProcessor proc = UpdateExecutionFactory.create(request, graphStore);
//    adding triples
//        modelTDB.add( ... );
//    file reading into model
//        InputStream inFile = FileManager.get().open("somefile.rdf");
//        modelTDB.read(inFile, defaultNameSpace);
//    copy in-memory model to dataset model
//        modelTDB.add( modelMem );
//

public static void main(String[] args)
{
	Dataset dataset=TDBFactory.createDataset("MyDatabases/Dataset1");

	dataset.begin(ReadWrite.WRITE);
	try
 	{
		Model model=dataset.getNamedModel("myrdf");

		// ... model stuff that adds triples ...

		dataset.commit();
	}
	finally
	{
		dataset.end();
	}

	// ...repeat as needed...READ or WRITE
}