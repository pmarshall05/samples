set :application, "tvlistings"
set :stage, "api"
set :repository, ""
set :deploy_to, ""
set :user, ""
role :app, ""
ssh_options[:keys] = [ENV['AWS_PEM']]
set :branch do
  default_tag = "#{git_tag}"

  if default_tag.empty?
    default_tag = "#{branch}"
  end

  if default_tag.empty?
    tag = capture("cd #{deploy_to} && git fetch --tags && git ls-remote #{repository} | git tag | sort -n | tail -n 1")
    tag = tag.split("\n").first
  else
    tag = "#{default_tag}"
  end
  tag
end
