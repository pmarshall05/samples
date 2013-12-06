module AwsApi
  def add_instance_to_elb(access_key_id, secret_access_key, environment, host)
    elb = AWS::ELB.new(
      :access_key_id => "#{access_key_id}",
      :secret_access_key => "#{secret_access_key}",
      :region => "us-west-2")

    ec2 = AWS::EC2.new(
      :access_key_id => "#{access_key_id}",
      :secret_access_key => "#{secret_access_key}",
      :region => "us-west-2")

    instance_id = ""

    ec2.instances.each do |instance|
      if instance.dns_name.eql?(host)
        instance_id = instance.id
      end
    end

    # get load blanacer
    puts "Environment: #{environment}; Instance: #{instance_id}"
    if environment.eql?('dev')
      load_balancer = elb.load_balancers['Zap2it-Dev']
    elsif environment.eql?('qa')
      load_balancer = elb.load_balancers['Zap2it-QA']
    elsif environment.eql?('qa2')
      load_balancer = elb.load_balancers['Zap2it-QA2']
    elsif environment.eql?('staging')
      load_balancer = elb.load_balancers['Zap2it-Stage']
    elsif environment.eql?('api')
      load_balancer = elb.load_balancers['Zap2it-Prod-Api']
    elsif environment.eql?('standard')
      load_balancer = elb.load_balancers['Zap2it-Prod-Standard']
    elsif environment.eql?('advanced')
      load_balancer = elb.load_balancers['Zap2it-Prod-Advanced']
    else
      load_balancer = elb.load_balancers['Zap2it-Prod-Flagship']
    end

    #register instance
    load_balancer.instances.register("#{instance_id}")
  end

  def remove_instance_from_elb(access_key_id, secret_access_key, environment, host)
    elb = AWS::ELB.new(
      :access_key_id => "#{access_key_id}",
      :secret_access_key => "#{secret_access_key}",
      :region => "us-west-2")

    ec2 = AWS::EC2.new(
      :access_key_id => "#{access_key_id}",
      :secret_access_key => "#{secret_access_key}",
      :region => "us-west-2")

    instance_id = ""

    ec2.instances.each do |instance|
      if instance.dns_name.eql?(host)
        instance_id = instance.id
      end
    end

     # get load blanacer
     puts "Environment: #{environment}; Instance: #{instance_id}"
    if environment.eql?('dev')
      load_balancer = elb.load_balancers['Zap2it-Dev']
    elsif environment.eql?('qa')
      load_balancer = elb.load_balancers['Zap2it-QA']
    elsif environment.eql?('qa2')
      load_balancer = elb.load_balancers['Zap2it-QA2']
    elsif environment.eql?('staging')
      load_balancer = elb.load_balancers['Zap2it-Stage']
    elsif environment.eql?('api')
      load_balancer = elb.load_balancers['Zap2it-Prod-Api']
    elsif environment.eql?('standard')
      load_balancer = elb.load_balancers['Zap2it-Prod-Standard']
    elsif environment.eql?('advanced')
      load_balancer = elb.load_balancers['Zap2it-Prod-Advanced']
    else
      load_balancer = elb.load_balancers['Zap2it-Prod-Flagship']
    end

    #deregister instance
    load_balancer.instances.deregister("#{instance_id}")
  end

  module_function :add_instance_to_elb
  module_function :remove_instance_from_elb
end
