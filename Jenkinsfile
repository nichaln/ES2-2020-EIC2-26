def dockeruser = "nanokiscteiul"
def imagename = "imagemProjeto"
def container = "projeto"
node {
   echo 'Building Docker Image'

stage('Git Checkout') {
    git 'https://github.com/nanok-iscteiul/ES2-2020-EIC2-26'
    }
    
stage('Build Image From the Dockerfile'){
     powershell "docker build -t  ${imagename} ."
    }
    
stage ('Runing Container to test built Docker Image'){
    powershell "docker run -dit --name ${container} -p 80:80 ${imagename}"
    }
}
