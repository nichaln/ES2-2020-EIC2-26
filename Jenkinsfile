pipeline {
  agent any
  stages {
    stage('compile') {
      steps {
        build 'Projeto-Compile'
      }
    }

    stage('javadoc') {
      steps {
        build 'Projeto-Javadoc'
      }
    }

  }
}