// C:\SWDevAppSoftware\MongoDB\Server\3.4\bin>mongo.exe 127.0.0.1/pivotalembrace C:\MicG_SW_DEV\DEV_Projects\PivotalEmbrace\src\main\resources\mongoDB\scripts\insertCounters.js

var quotesSeq = {
    _id  : "quotesid",
    sequence_value : 1
};

var goalSeq = {
    _id  : "goalid",
    sequence_value : 1
};

var taskToDoSeq = {
    _id  : "task_to_doid",
    sequence_value : 1
};

db.counters.insert(quotesSeq);
db.counters.insert(goalSeq);
db.counters.insert(taskToDoSeq);