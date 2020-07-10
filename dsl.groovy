job("Groovy 1")

{

 description ("This is my first job for Groovy project")

scm {

github ('sumayya-github/jen-groovy','master')

}

steps{

shell('sudo cp -r -v -f * /t6 ')

}

}

job("Groovy 2")

{

description ("This is my second job for Groovy project ")

steps{

shell('''sudo kubectl version

if sudo ls /t6 | grep apache

then

 if sudo kubectl get svc | grep apache-svc

 then

 echo "Service for apache is Running"

 else

 sudo kubectl create -f /t6/apache_svc.yml 

 fi

 if sudo kubectl get pvc | grep apache-pvc

 then

   echo " PVC for apache is already running"

 else

   sudo kubectl create -f /t6/apache_pvc.yml 

 fi

 if sudo kubectl get deploy | grep apache_deploy
 
 then
 
 echo "Deployment for apache running"

 else
 
 sudo  kubectl create -f /t6/apache_deploy.yml

else 

echo "no html code from developer to host"

fi


}

 triggers {

       upstream('Groovy 1', 'SUCCESS')

   }

}

job("Groovy 3")

{

description ("This is the job 3 for groovy project")

steps{

shell(''' status=$(curl -o /dev/null -sw "%{http_code}" http://10.0.2.15/index.html)

if [[$status == 200 ]]

then

exit 1

else

exit 0

fi ''')

}

triggers {

       upstream('Groovy 2', 'SUCCESS')

   }

}

job("Groovy 4")

{

description ("This is mailing job")

 authenticationToken('mail')

 

 publishers {

       mailer('sumayyakhatoon58@gmail.com', true, true)

   }

triggers {

       upstream('Groovy 3', 'SUCCESS')

   }

}
