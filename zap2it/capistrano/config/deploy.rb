set :application, "zcore"
set :scm, :git
set :deploy_via, :remote_cache
set :stages, ["dev", "qa", "qa2", "staging", "standard", "advanced", "flagship", "api"]
set :tomcat_base, "/var/lib/tomcat6/"
set :tomcat_lib, "/var/lib/tomcat6/lib"
set :tomcat_webapps, "/var/lib/tomcat6/webapps"
set :access_key_id, ENV['AWS_ACCESS_KEY_ID']
set :secret_access_key, ENV['AWS_SECRET_ACCESS_KEY']

after "deploy:rollback:revision" do
  if application == 'core_lib'
    run "for f in $(ls -d #{tomcat_lib}/*); do #{try_sudo} unlink $f; done"
    run "for f in $(ls -d #{current_path}/dist/*); do #{try_sudo} ln -s $f #{tomcat_lib}; done && ls -al #{tomcat_lib}"
  else
    run "cd #{tomcat_webapps} && #{try_sudo} rm -rf */"
    run "unlink #{tomcat_webapps}/tvlistings.war"
    run "unlink #{tomcat_webapps}/movies.war"
    run "unlink #{tomcat_webapps}/people.war"
    run "unlink #{tomcat_webapps}/zim.war"
    run "unlink #{tomcat_webapps}/ehcacheListener.war"
    run "rm -f #{tomcat_lib}/tmsCatalinaOverrides.jar"
    run "ln -s #{current_path}/dist/tvlistings.war #{tomcat_webapps}/tvlistings.war"
    run "ln -s #{current_path}/dist/movies.war #{tomcat_webapps}/movies.war"
    run "ln -s #{current_path}/dist/people.war #{tomcat_webapps}/people.war"
    run "ln -s #{current_path}/dist/zim.war #{tomcat_webapps}/zim.war"
    run "ln -s #{current_path}/dist/ehcacheListener.war #{tomcat_webapps}/ehcacheListener.war"
    run "ln -s #{current_path}/dist/tmsCatalinaOverrides.jar #{tomcat_lib}/tmsCatalinaOverrides.jar"
    run "cd #{current_path} && ant generateLabelJSfiles && ant minify && ant minify-checkin"
      
    if "#{stage}".eql?("dev") || "#{stage}".eql?("qa") || "#{stage}".eql?("qa2")  
      run "cd #{current_path} && cp -r web/stylesheets/* /opt/www/static.#{stage}.zap2it.com/css/"
      run "cd #{current_path} && cp -r dist/javascript/* /opt/www/static.#{stage}.zap2it.com/javascript/"
    elsif "#{stage}".eql?("staging")
      run "cd #{current_path} && cp -r web/stylesheets/* /opt/www/static.stage.zap2it.com/css/"
      run "cd #{current_path} && cp -r dist/javascript/* /opt/www/static.stage.zap2it.com/javascript/"
    else
      run "cd #{current_path} && cp -r web/stylesheets/* /opt/www/static.zap2it.com/css/"
      run "cd #{current_path} && cp -r dist/javascript/* /opt/www/static.zap2it.com/javascript/"
    end
  end
end

namespace :deploy do
  desc <<-DESC
    	Deploys your project. This calls both `update' and `restart'. Note that \
    	this will generally only work for applications that have already been deployed \
    	once. For a "cold" deploy, you'll want to take a look at the `deploy:cold' \
    	task, which handles the cold start specifically.
  DESC
  task :default do
    update
    zcore.chown
    zcore.compile
    zcore.install
    
    if application == 'tvlistings'
      restart
    end
  end
  
  desc <<-DESC
    	Prepares one or more servers for deployment. Before you can use any \
    	of the Capistrano deployment tasks with your project, you will need to \
    	make sure all of your servers have been prepared with `cap deploy:setup'. When \
    	you add a new server to your cluster, you can easily run the setup task \
    	on just that server by specifying the HOSTS environment variable:

      	$ cap HOSTS=new.server.com deploy:setup

    	It is safe to run this task on servers that have already been set up; it \
    	will not destroy any deployed revisions or data.
  DESC
  task :setup do
    dirs = [deploy_to, releases_path, shared_path, current_path]
    dirs += shared_children.map { |d| File.join(shared_path, d.split('/').last) }
    run "#{try_sudo} mkdir -p #{dirs.join(' ')}"
    run "#{try_sudo} chmod g+w #{dirs.join(' ')}" if fetch(:group_writable, true)
     
    run "rm -rf #{current_path} && #{try_sudo} ln -s #{deploy_to}/ #{current_path}"
      
    if application == 'core_lib'
      run "cd #{deploy_to}/lib && cp ojdbc14.jar #{current_path}/dist && cp mysql-connector-java-5.1.14-bin.jar #{current_path}/dist"
      run "cd #{tomcat_lib} && rm -rf *"
      run "for f in $(ls -d #{current_path}/dist/*); do #{try_sudo} ln -s $f #{tomcat_lib}; done && ls -al #{tomcat_lib}"
    else
      run "cd #{tomcat_webapps} && rm -rf *"
      run "ln -s #{current_path}/dist/tvlistings.war #{tomcat_webapps}/tvlistings.war"
      run "ln -s #{current_path}/dist/movies.war #{tomcat_webapps}/movies.war"
      run "ln -s #{current_path}/dist/people.war #{tomcat_webapps}/people.war"
      run "ln -s #{current_path}/dist/zim.war #{tomcat_webapps}/zim.war"
      run "ln -s #{current_path}/dist/ehcacheListener.war #{tomcat_webapps}/ehcacheListener.war"
      run "ln -s #{current_path}/dist/tmsCatalinaOverrides.jar #{tomcat_lib}/tmsCatalinaOverrides.jar"
    end
    	
    zcore.chown
    
    if application == 'tvlistings'
      deploy.restart
    end
  end
  
  desc <<-DESC
   		[internal] Touches up the released code. This is called by update_code \
    	after the basic deploy finishes. It assumes a Rails project was deployed, \
    	so if you are deploying something else, you may want to override this \
    	task with your own environment's requirements.

    	This task will make the release group-writable (if the :group_writable \
    	variable is set to true, which is the default). It will then set up \
    	symlinks to the shared directory for the log, system, and tmp/pids \
    	directories, and will lastly touch all assets in public/images, \
    	public/stylesheets, and public/javascripts so that the times are \
    	consistent (so that asset timestamping works).  This touch process \
    	is only carried out if the :normalize_asset_timestamps variable is \
    	set to true, which is the default. The asset directories can be overridden \
    	using the :public_children variable.
 	DESC
  task :finalize_update do
    escaped_release = latest_release.to_s.shellescape
    commands = []
    commands << "chmod -R -- g+w #{escaped_release}" if fetch(:group_writable, true)

    # mkdir -p is making sure that the directories are there for some SCM's that don't
    # save empty folders
    shared_children.map do |dir|
      d = dir.shellescape
      if (dir.rindex('/')) then
        commands += ["rm -rf -- #{escaped_release}/#{d}",
          "mkdir -p -- #{escaped_release}/#{dir.slice(0..(dir.rindex('/'))).shellescape}"]
      else
        commands << "rm -rf -- #{escaped_release}/#{d}"
      end
      	
      commands << "ln -s -- #{shared_path}/#{dir.split('/').last.shellescape} #{escaped_release}/#{d}"
    end

    run commands.join(' && ') if commands.any?

	end

	desc <<-DESC
    Blank task exists as a hook into which to install your own environment \
    specific behaviour.
  DESC
  task :restart, :roles => :app do
    tomcat.stop
    tomcat.restart
    deploy.cleanup
  end

	desc <<-DESC
    Clean up old releases. By default, the last 5 releases are kept on each \
    server (though you can change this with the keep_releases variable). All \
    other deployed revisions are removed from the servers. By default, this \
    will use sudo to clean up the old releases, but if sudo is not available \
    for your environment, set the :use_sudo variable to false instead.
  DESC
  task :cleanup, :roles => :app do
    count = fetch(:keep_releases, 5).to_i
    try_sudo "ls -1dt #{releases_path}/* | tail -n +#{count + 1} | #{try_sudo} xargs rm -rf"
	end
end

namespace :tomcat do

	desc "Start tomcat"
	task :startall, :roles => [:app] do
    find_servers(:roles => :app).each do |server|
      ENV['HOSTFILTER'] = server.host
	
      puts "================== Start Tomcat ======================"
      run "sudo /etc/init.d/tomcat6 start"
	
      totalSleep = 0;
      code = "0"
	     
      puts "========= Sleeping until TVListings on #{server.host} is up again ========="
      until code.eql?("200") || totalSleep == 40 do
        sleep(10)
        totalSleep += 10
        code = `curl -s -o -I -w %{http_code} http://#{server.host}:8080/tvlistings/ZCGrid.do`
        puts "Response Code: #{code}"
      end
	      
      totalSleep = 0;
      code = "0"
	      
      puts "========= Sleeping until Movies on #{server.host} is up again ========="
      until code.eql?("200") || totalSleep == 300 do
        sleep(10)
        totalSleep += 10
        code = `curl -s -o -I -w %{http_code} http://#{server.host}:8080/movies/mvhome.do`
        puts "Response Code: #{code}"
      end  
	
      totalSleep = 0;
      code = "0"
	
			puts "========= Sleeping until People on #{server.host} is up again ========="
      until code.eql?("200") || totalSleep == 300 do
        sleep(10)
        totalSleep += 10
        code = `curl -s -o -I -w %{http_code} http://#{server.host}:8080/people/celebsontv.do`
        puts "Response Code: #{code}"
      end  
	      
      puts "========= Adding #{server.host} to the  #{stage} Load Balancer ========= "
      AwsApi.add_instance_to_elb("#{access_key_id}", "#{secret_access_key}", "#{stage}","#{server.host}")
		end
  end
  
 	desc "Stop tomcat"
	task :stopall, :roles => [:app] do
    find_servers(:roles => :app).each do |server|
      ENV['HOSTFILTER'] = server.host
	  		
      puts "========= Removing #{server.host} from the #{stage} Load Balancer ========= "
      AwsApi.remove_instance_from_elb("#{access_key_id}", "#{secret_access_key}", "#{stage}","#{server.host}")
      puts "Sleeping for 15 seconds until #{server.host} is down"
      sleep(15)
	    	
      puts "================== Stop Tomcat ======================"
      run "sudo /etc/init.d/tomcat6 stop"
	 	end
  end
  
  desc "Restart all tomcat in an environment of group of hosts"
  task :restartall, :roles => [:app] do
    find_servers(:roles => :app).each do |server|
      ENV['HOSTFILTER'] = server.host
	  		
      puts "========= Removing #{server.host} from the #{stage} Load Balancer ========= "
      AwsApi.remove_instance_from_elb("#{access_key_id}", "#{secret_access_key}", "#{stage}","#{server.host}")
      puts "Sleeping for 15 seconds until #{server.host} is down"
      sleep(15)
	      	
      puts "================== Restart Tomcat on #{server.host} ======================"
      run "sudo /etc/init.d/tomcat6 restart"
	    	
      totalSleep = 0;
      code = "0"
	     
      puts "========= Sleeping until TVListings on #{server.host} is up again ========="
      until code.eql?("200") || totalSleep == 40 do
        sleep(10)
        totalSleep += 10
        code = `curl -s -o -I -w %{http_code} http://#{server.host}:8080/tvlistings/ZCGrid.do`
        puts "Response Code: #{code}"
      end
	      
      totalSleep = 0;
      code = "0"
	      
      puts "========= Sleeping until Movies on #{server.host} is up again ========="
      until code.eql?("200") || totalSleep == 300 do
        sleep(10)
        totalSleep += 10
        code = `curl -s -o -I -w %{http_code} http://#{server.host}:8080/movies/mvhome.do`
        puts "Response Code: #{code}"
      end  
	      
      totalSleep = 0;
      code = "0"
	
      puts "========= Sleeping until People on #{server.host} is up again ========="
      until code.eql?("200") || totalSleep == 300 do
        sleep(10)
        totalSleep += 10
        code = `curl -s -o -I -w %{http_code} http://#{server.host}:8080/people/celebsontv.do`
        puts "Response Code: #{code}"
      end  

      puts "========= Adding #{server.host} to the  #{stage} Load Balancer ========= "
      AwsApi.add_instance_to_elb("#{access_key_id}", "#{secret_access_key}", "#{stage}","#{server.host}")
    end
  end
  	
  task :stop do
    puts "================== Stop Tomcat on ======================"
    run "sudo /etc/init.d/tomcat6 stop"
  end
  	
  task :start do
    puts "================== Start Tomcat on ======================"
    run "sudo /etc/init.d/tomcat6 start"
  end
  	
  task :restart do
    puts "================== Restart Tomcat on ======================"
    run "sudo /etc/init.d/tomcat6 restart"
  end
end


namespace :zcore do
  desc "Compile the ZCore webapp (corelib and tvlistings)"
  task :compile, :roles => [:app] do
    puts "================== Compile #{application} #{branch}======================"
    run "cd #{current_path} && ant dist"


    if application == 'core_lib'
      run "cd #{deploy_to}/lib && cp ojdbc14.jar #{current_path}/dist && cp mysql-connector-java-5.1.14-bin.jar #{current_path}/dist"
    end
  end

  desc "Install the ZCore webapp (corelib and tvlistings)"
  task :install, :roles => [:app] do
    puts "================== Install #{application} #{branch} ======================"
    if application == 'core_lib'
      run "for f in $(ls -d #{tomcat_lib}/*); do #{try_sudo} unlink $f; done"
      run "for f in $(ls -d #{current_path}/dist/*); do #{try_sudo} ln -s $f #{tomcat_lib}; done && ls -al #{tomcat_lib}"
    else
      run "cd #{tomcat_webapps} && #{try_sudo} rm -rf */"
      run "unlink #{tomcat_webapps}/tvlistings.war"
      run "unlink #{tomcat_webapps}/movies.war"
      run "unlink #{tomcat_webapps}/people.war"
      run "unlink #{tomcat_webapps}/zim.war"
      run "unlink #{tomcat_webapps}/ehcacheListener.war"
      run "rm -f #{tomcat_lib}/tmsCatalinaOverrides.jar"
      run "ln -s #{current_path}/dist/tvlistings.war #{tomcat_webapps}/tvlistings.war"
      run "ln -s #{current_path}/dist/movies.war #{tomcat_webapps}/movies.war"
      run "ln -s #{current_path}/dist/people.war #{tomcat_webapps}/people.war"
      run "ln -s #{current_path}/dist/zim.war #{tomcat_webapps}/zim.war"
      run "ln -s #{current_path}/dist/ehcacheListener.war #{tomcat_webapps}/ehcacheListener.war"
      run "ln -s #{current_path}/dist/tmsCatalinaOverrides.jar #{tomcat_lib}/tmsCatalinaOverrides.jar"
      run "cd #{current_path} && ant generateLabelJSfiles && ant minify && ant minify-checkin"

      if "#{stage}".eql?("dev") || "#{stage}".eql?("qa") || "#{stage}".eql?("qa2")
        run "cd #{current_path} && cp -r web/stylesheets/* /opt/www/static.#{stage}.zap2it.com/css/"
        run "cd #{current_path} && cp -r dist/javascript/* /opt/www/static.#{stage}.zap2it.com/javascript/"
      elsif "#{stage}".eql?("staging")
        run "cd #{current_path} && cp -r web/stylesheets/* /opt/www/static.stage.zap2it.com/css/"
        run "cd #{current_path} && cp -r dist/javascript/* /opt/www/static.stage.zap2it.com/javascript/"
      else
        run "cd #{current_path} && cp -r web/stylesheets/* /opt/www/static.zap2it.com/css/"
        run "cd #{current_path} && cp -r dist/javascript/* /opt/www/static.zap2it.com/javascript/"
      end
    end
  end

  desc "Change owner"
  task :chown, :roles => [:app] do
    run "sudo chown -R zptvl:zpnat #{deploy_to}"
  end
end

namespace :setup_all do
  task :default, :roles => [:app] do
    find_servers(:roles => :app).each do |server|
      ENV['HOSTFILTER'] = server.host

      puts "Deploy ZCore -- Environment: #{stage}"
      puts "========= Removing #{server.host} from  the #{stage} Load Balancer ========= "
      AwsApi.remove_instance_from_elb("#{access_key_id}", "#{secret_access_key}", "#{stage}","#{server.host}")
      puts "Sleeping for 15 seconds until #{server.host} is down"
      sleep(15)
      Capistrano::CLI.ui.say "setup zcore:core_lib:#{stage}"
      system("cap zcore:core_lib:#{stage} deploy:setup")

      Capistrano::CLI.ui.say "setup zcore:tvlistings:#{stage}"
      system("cap zcore:tvlistings:#{stage} deploy:setup")

      totalSleep = 0;
      code = "0"

      puts "========= Sleeping until TVListings on #{server.host} is up again ========="
      until code.eql?("200") || totalSleep == 40 do
        sleep(10)
        totalSleep += 10
        code = `curl -s -o -I -w %{http_code} http://#{server.host}:8080/tvlistings/ZCGrid.do`
        puts "Response Code: #{code}"
      end

      if totalSleep == 40
        Capistrano::CLI.ui.say "rollback zcore:core_lib:#{stage}"
        system("cap zcore:core_lib:#{stage} rollback")

        Capistrano::CLI.ui.say "rollback zcore:tvlistings:#{stage}"
        system("cap zcore:tvlistings:#{stage} rollback")
      end

      totalSleep = 0;
      code = "0"

      puts "========= Sleeping until Movies on #{server.host} is up again ========="
      until code.eql?("200") || totalSleep == 300 do
        sleep(10)
        totalSleep += 10
        code = `curl -s -o -I -w %{http_code} http://#{server.host}:8080/movies/mvhome.do`
        puts "Response Code: #{code}"
      end

      if totalSleep == 300
        Capistrano::CLI.ui.say "rollback zcore:core_lib:#{stage}"
        system("cap zcore:core_lib:#{stage} rollback")

        Capistrano::CLI.ui.say "rollback zcore:tvlistings:#{stage}"
        system("cap zcore:tvlistings:#{stage} rollback")
      end
      
      totalSleep = 0;
      code = "0"

      puts "========= Sleeping until People on #{server.host} is up again ========="
      until code.eql?("200") || totalSleep == 300 do
        sleep(10)
        totalSleep += 10
        code = `curl -s -o -I -w %{http_code} http://#{server.host}:8080/people/celebsontv.do`
        puts "Response Code: #{code}"
      end

      if totalSleep == 300
        Capistrano::CLI.ui.say "rollback zcore:core_lib:#{stage}"
        system("cap zcore:core_lib:#{stage} rollback")

        Capistrano::CLI.ui.say "rollback zcore:tvlistings:#{stage}"
        system("cap zcore:tvlistings:#{stage} rollback")
      end

      puts "========= Adding #{server.host} to the  #{stage} Load Balancer ========= "
      AwsApi.add_instance_to_elb("#{access_key_id}", "#{secret_access_key}", "#{stage}","#{server.host}")
    end
  end
end

namespace :deploy_all do
  set :deploy_zcore_all, true
  task :default, :roles => [:app] do
    find_servers(:roles => :app).each do |server|
      ENV['HOSTFILTER'] = server.host

      puts "Deploy ZCore -- Environment: #{stage}"
      puts "========= Removing #{server.host} from the  #{stage} Load Balancer ========= "
      AwsApi.remove_instance_from_elb("#{access_key_id}", "#{secret_access_key}", "#{stage}","#{server.host}")
      puts "Sleeping for 15 seconds until #{server.host} is down"
      sleep(15)

      Capistrano::CLI.ui.say "deploying zcore:core_lib:#{stage}"
      system("cap zcore:core_lib:#{stage} deploy -s git_tag=#{git_tag}")

      Capistrano::CLI.ui.say "deploying zcore:tvlistings:#{stage}"
      system("cap zcore:tvlistings:#{stage} deploy -s git_tag=#{git_tag}")

      totalSleep = 0;
      code = "0"
      error = 0;
      errormsg = nil;

      puts "========= Sleeping until TVListings on #{server.host} is up again ========="
      until code.eql?("200") || totalSleep == 40 || error > 0 do
        sleep(10)
        totalSleep += 10
        code = `curl -s -o -I -w %{http_code} http://#{server.host}:8080/tvlistings/ZCGrid.do`
        error += `curl -s "http://#{server.host}:8080/tvlistings/ZCGrid.do" | grep -c "Oops..."`.to_i
        errormsg = CommandError.new("Received error page on TV Listings")
	      puts "Response Code: #{code}; Error count: #{error}"
      end
      
      if totalSleep == 40 || error > 0
        Capistrano::CLI.ui.say "rollback zcore:core_lib:#{stage}"
        system("cap zcore:core_lib:#{stage} deploy:rollback")
   
        Capistrano::CLI.ui.say "rollback zcore:tvlistings:#{stage}"
        system("cap zcore:tvlistings:#{stage} deploy:rollback")
      end
      
      if error > 0
        raise errormsg
      end

      totalSleep = 0;
      code = "0"
      error = 0;
      errormsg = nil;
      
      puts "========= Sleeping until Movies on #{server.host} is up again ========="
      until code.eql?("200") || totalSleep == 300 || error > 0  do
        sleep(10)
        totalSleep += 10
        code = `curl -s -o -I -w %{http_code} http://#{server.host}:8080/movies/mvhome.do`
        error += `curl -s "http://#{server.host}:8080/movies/mvhome.do" | grep -c "Oops..."`.to_i
        errormsg = CommandError.new("Received error page on Movies")
	      puts "Response Code: #{code}; Error count: #{error}"
      end  
      
      if totalSleep == 300 || error > 0
        Capistrano::CLI.ui.say "rollback zcore:core_lib:#{stage}"
        system("cap zcore:core_lib:#{stage} deploy:rollback")
   
        Capistrano::CLI.ui.say "rollback zcore:tvlistings:#{stage}"
        system("cap zcore:tvlistings:#{stage} deploy:rollback")
      end
      
      if error > 0
        raise errormsg
      end
      
      totalSleep = 0;
      code = "0"
      error = 0;
      errormsg = nil;

      puts "========= Sleeping until People on #{server.host} is up again ========="
      until code.eql?("200") || totalSleep == 300 || error > 0 do
        sleep(10)
        totalSleep += 10
        code = `curl -s -o -I -w %{http_code} http://#{server.host}:8080/people/celebsontv.do`
        error += `curl -s "http://#{server.host}:8080/people/celebsontv.do" | grep -c "Oops..."`.to_i
        errormsg = CommandError.new("Received error page on People")
        puts "Response Code: #{code}; Error count: #{error}"
      end  
      
      if totalSleep == 300 || error > 0
        Capistrano::CLI.ui.say "rollback zcore:core_lib:#{stage}"
        system("cap zcore:core_lib:#{stage} deploy:rollback")
   
        Capistrano::CLI.ui.say "rollback zcore:tvlistings:#{stage}"
        system("cap zcore:tvlistings:#{stage} deploy:rollback")
      end
      
      if error > 0
        raise errormsg
      end

      puts "========= Adding #{server.host} to the #{stage} Load Balancer ========= "
      AwsApi.add_instance_to_elb("#{access_key_id}", "#{secret_access_key}", "#{stage}","#{server.host}")
    end
  end
end

namespace :rollback_all do
  task :default, :roles => [:app] do
    find_servers(:roles => :app).each do |server|
      ENV['HOSTFILTER'] = server.host

      puts "Deploy ZCore -- Environment: #{stage}"
      puts "========= Removing #{server.host} from Load Balancer ========= "
      AwsApi.remove_instance_from_elb("#{access_key_id}", "#{secret_access_key}", "#{stage}","#{server.host}")
      puts "Sleeping for 15 seconds until #{server.host} is down"
      sleep(15)

      Capistrano::CLI.ui.say "rollback zcore:core_lib:#{stage}"
      system("cap zcore:core_lib:#{stage} rollback")

      Capistrano::CLI.ui.say "rollback zcore:tvlistings:#{stage}"
      system("cap zcore:tvlistings:#{stage} rollback")

      puts "Sleeping until #{server.host} is up again"
      code = "0"
      until code.eql?("200") do
        sleep(10)
        code = `curl -s -o -I -w %{http_code} http://#{server.host}:8080/tvlistings/ZCGrid.do`
        puts "Response Code: #{code}"
      end

      puts "========= Adding #{server.host} to Load Balancer ========= "
      AwsApi.add_instance_to_elb("#{access_key_id}", "#{secret_access_key}", "#{stage}","#{server.host}")
    end
  end
end
