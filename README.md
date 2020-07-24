# pipeline-samples
These are simple exercises where there are pipelines and functions defined, that explore the usage of Apache Beam

## Run on local

```
mvn package
java -jar target/pipelines-samples-0.1-shaded.jar
```

## Run on Google Dataflow
```
export GOOGLE_APPLICATION_CREDENTIALS="/Users/{user}/{somePath}/XXX_credentials.json"
gcloud auth application-default login
mvn package -Pdataflow-runner
java -jar target/pipelines-samples-0.1-shaded.jar --runner=DataflowRunner --project=deloitte-beam-284202 --tempLocation=gs://deloitte-beam-sandbox/temp/ --region=us-west1
```

## Run on Amazon EMR (Flink)
```
mvn package -Pflink-runner
scp -i ~/.ssh/keypair.pem ./target/pipeline-samples-0.1-shaded.jar ec2-user@ec2-xxx-xxx-xxx:/home/hadoop
```

## Run on local Flink cluster
```
mvn package -Pflink-runner
./bin/flink run /Users/user}/{somePath}/pipeline-samples/target/pipelines-samples-0.1-shaded.jar --runner=FlinkRunner
```