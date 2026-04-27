# Jenkins CI/CD

This project now includes a root [`Jenkinsfile`](/Users/shenxinyi/Projects/final-project/Jenkinsfile) for CI/CD.

## What the pipeline does

1. Builds and tests the Java services with Maven on Java 17.
2. Builds and tests the Angular frontend on Node 22.
3. Builds Docker images for:
   - `order-service`
   - `payment-service`
   - `frontend`
4. Optionally pushes those images to a registry.
5. Optionally deploys the new image tags to Kubernetes.

## Jenkins prerequisites

- A Jenkins agent that can run Docker commands.
- Docker Pipeline support in Jenkins, because the test stages use containerized build agents.
- `kubectl` installed on the Jenkins agent if you want to use the deploy stage.
- A Pipeline job pointed at this repository.

## Recommended Jenkins credentials

- `docker-registry-creds`
  Use a Jenkins `Username with password` credential for registry login.
- `kubeconfig-final-project`
  Use a Jenkins `Secret file` credential containing a kubeconfig with access to the target cluster.

You can keep those default IDs or change them with build parameters.

## Default build parameters

- `PUSH_IMAGES=false`
- `DEPLOY_TO_K8S=false`
- `DOCKER_REGISTRY=""`
- `DOCKER_IMAGE_TAG=""`
- `DOCKER_CREDENTIALS_ID="docker-registry-creds"`
- `KUBECONFIG_CREDENTIALS_ID="kubeconfig-final-project"`
- `K8S_NAMESPACE="final-project"`

If `DOCKER_IMAGE_TAG` is left empty, Jenkins uses the build number.

## Typical usage

CI only:

- Run the pipeline with the defaults.

Build and push images:

- Set `PUSH_IMAGES=true`
- Set `DOCKER_REGISTRY` to your registry namespace, for example `123456789012.dkr.ecr.us-west-2.amazonaws.com/final-project`

Build, push, and deploy:

- Set `PUSH_IMAGES=true`
- Set `DEPLOY_TO_K8S=true`
- Set `DOCKER_REGISTRY`
- Make sure the kubeconfig credential points at the right cluster

## Deployment behavior

The deploy stage applies [`k8s/app.yaml`](/Users/shenxinyi/Projects/final-project/k8s/app.yaml) and then updates the three application deployments with the freshly built image tags using `kubectl set image`.

That means you do not need to hard-code versioned image tags in the Kubernetes manifest for every release.

## Notes

- The pipeline keeps image push and Kubernetes deploy disabled by default so it is safe to add before registry or cluster credentials are ready.
- The frontend CI environment uses Node 22 to match the Docker image build more closely than an arbitrary Jenkins host Node install.
- The backend test stage uses Maven with Java 17, which matches the service Dockerfiles and project configuration.
