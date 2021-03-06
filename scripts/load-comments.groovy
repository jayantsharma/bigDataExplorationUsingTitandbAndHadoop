PROJECT_DIR = "/home/jayant/work/masters/course_work/big_data_engg/nosql_project/titan-1.0.0-hadoop1"

g = TitanFactory.open(PROJECT_DIR + "/conf/se_dump.properties")
m = g.openManagement()

// Create Vertex label : comment
comment = m.makeVertexLabel("comment").make()

// The property Score already exists, so nothing to do about that.

m.commit()
g.close()

// Specifies settings for Hadoop-Gremlin - the processing powerhouse of Tinkerpop 
graph = GraphFactory.open(PROJECT_DIR + "/conf/hadoop-graph/comments-hadoop-load.properties")

// Graph Engine settings
writeGraph = PROJECT_DIR + "/conf/se_dump.properties"
blvp = BulkLoaderVertexProgram.build().keepOriginalIds(true).writeGraph(writeGraph).
        intermediateBatchSize(10000).create(graph)
graph.compute(SparkGraphComputer).program(blvp).submit().get()
