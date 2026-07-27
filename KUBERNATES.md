--Docker---------------------------------------------------------------

Check the docker version

docker --version

Install Docker Engine if needed:

sudo apt install docker.io

Start Docker:

sudo systemctl start docker

Enable it on boot:

sudo systemctl enable docker

If you want to stop Docker completely on Ubuntu:

sudo systemctl stop docker

Verify its status:

sudo systemctl status docker

--------MINICUBES -------------------------------------------------------------------------------------------------

its OFFER TO CREATE SINGLE MASTER AND WORKER NODE IN LOCAL ENVIRONMENT, TO START MINIKUBES WE NEED A DOCKER

Minikube (Restaurant) manages those containers using Kubernetes.(Kubernetes Cluster)
kubectl (Manager) receives your instructions and tells Kubernetes what to do.
 --------------------------------------------------------------------------------
## Start Minikube
$ minikube start
 

## Namespace

Its like packages name in Any project
