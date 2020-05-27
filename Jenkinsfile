def dockeruser = "nanokiscteiul"
def mysqlimage = "ubuntu:16"
def wordpressimage = "wordpress:latest"
def container = "wordpress"
node {
   echo 'Building Apache Docker Image'

stage('Git Checkout') {
    git 'https://github.com/nanok-iscteiul/ES2-2020-EIC2-26'
    }
    
stage('Build mysql Image'){
     powershell "docker build -t  ${mysqlimage} ."
    }
   
stage('Build wordpress image'){
     powershell "docker build -t ${wordpressimage} ." 
    }

stage('Stop Existing Container'){
     powershell "docker stop ${container}"
    }
    
stage('Remove Existing Container'){
     powershell "docker rm ${container}"
    }
    
stage ('Runing Container to test built Docker Image'){
    powershell "docker run -dit --name ${container} -p 80:80 ${imagename}"
    }
    
stage('Tag Docker Image'){
    powershell "docker tag ${imagename} ${env.dockeruser}/ubuntu:16.04"
    }

stage('Docker Login and Push Image'){
    withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', passwordVariable: 'dockerpasswd', usernameVariable: 'dockeruser')]) {
    powershell "docker login -u ${dockeruser} -p ${dockerpasswd}"
    }
    powershell "docker push ${dockeruser}/ubuntu:16.04"
    }
}
