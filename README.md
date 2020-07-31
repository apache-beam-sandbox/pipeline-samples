# pipeline-samples
These are simple exercises where there are pipelines and functions defined, that explore the usage of Apache Beam

## Run on local

```
mvn package
java -jar target/pipelines-samples-0.1-shaded.jar
```

## Run on local Flink cluster
```
mvn package -Pflink-runner
cd flink/flink-1.11.0
./bin/flink run /Users/user}/{somePath}/pipeline-samples/target/pipelines-samples-0.1-shaded.jar --runner=FlinkRunner
```


## Run on Docker Flink cluster

Package the jar file as a fat.jar - dependencies included - using the shade plugin

```
mvn package -Pflink-runner
```
This will create a jar file in the /target/pipelines-samples-0.1-shaded.jar directory

From a windows Power shell command, start flink with docker compose, with the following commands: 
```
set COMPOSE_CONVERT_WINDOWS_PATH=1
docker-compose up -d
```
Then bring up the Flink UI, I configured it for port 8888
localhost:8888

<img width="943" alt="flink-ui-snapshot" src="https://user-images.githubusercontent.com/3783738/89088727-aeb18200-d34e-11ea-8cb1-2fd80e8fdf73.png">


upload the pipelines-samples-0.1-shaded.jar file, add the program argument :

```
--runner=FlinkRunner
```
<img width="932" alt="flink-ui-upload-snapshot" src="https://user-images.githubusercontent.com/3783738/89088770-d99bd600-d34e-11ea-9500-1e2df44318f9.png">


You should be able to run your job, and see the results:
<img width="932" alt="screenshot-running-20" src="https://user-images.githubusercontent.com/3783738/89088791-f7693b00-d34e-11ea-9fdf-b48d237566aa.PNG">



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