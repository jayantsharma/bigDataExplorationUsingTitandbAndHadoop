import groovy.io.FileType

// Shorthand
PROJECT_DIR = "/home/jayant/work/masters/course_work/big_data_engg/nosql_project/titan-1.0.0-hadoop1"
// Importing some helper functions
// evaluate(new File("${PROJECT_DIR}/scripts/helpers.groovy"))
sourceFile = new File("${PROJECT_DIR}/scripts/helpers.groovy");
groovyClass = new GroovyClassLoader(getClass().getClassLoader()).parseClass(sourceFile);
helpers = (GroovyObject) groovyClass.newInstance();

graph = TitanFactory.open(PROJECT_DIR + "/conf/se_dump.properties")
m = graph.openManagement()
 
// Create Edge labels
commentOn = m.makeEdgeLabel("commentOn").make()

m.commit()
g = graph.traversal()

dir = new File("${PROJECT_DIR}/data/posts_comments")
i = 0
dir.eachFileRecurse (FileType.FILES) { file ->
  file.eachLine { line ->
    i += 1
    def parts = line.split(/,/, -1)
    v1 = helpers.getOrCreate(graph, g, 'bulkLoader.vertex.id', "comment:${parts[0]}")
    v2 = helpers.getOrCreate(graph, g, 'bulkLoader.vertex.id', "post:${parts[1]}")
    if(i % 10000 == 0)
      println("${i} -> ${v2.property('CreationDate')}")
    v1.addEdge('commentOn', v2)
  }
}
