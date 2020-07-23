# pipeline-samples


## Run on local

```
mvn package
java -jar ./target/pipeline-samples-0.1-shaded.jar
```

## Run on Google Dataflow
```
mvn package -Pdataflow-runner
java -jar ./target/pipeline-samples-0.1-shaded.jar --runner=DataflowRunner --project=xxxx --tempLocation=gs://<YOUR_GCS_BUCKET>/temp/
```

## Run on Amazon EMR (Flink)
```
mvn package -Pflink-runner
scp -i ~/.ssh/keypair.pem ./target/pipeline-samples-0.1-shaded.jar ec2-user@ec2-xxx-xxx-xxx:/home/hadoop
```

## Run on local Flink Cluster
```
JAR :command-runner.jar
./bin/flink run /Users/{UserName}/Projects/pipeline-samples/target/pipeline-samples-0.1--shaded.jar  --runner=FlinkRunner
```
