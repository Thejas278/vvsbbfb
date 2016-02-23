#!/usr/bin/env ruby

require 'fileutils'

def reset_source(project_path)
  Dir.chdir(project_path) do
    `git checkout -- **`
    `git clean -fdX`
    `git clean -fX`
    `git clean -fd`
    `git clean -f`
  end
end

def print_usage()
  puts("Usage: #{File.basename($0)} dbEnv (test-qa, test-local or dev) reset (optional)")
end

ecom_project_path = "#{File.dirname($0)}/../../eAccept-eCom"
ecomadmin_project_path = "#{File.dirname($0)}/../../eAccept-eComAdmin"
projects_paths = [ecom_project_path, ecomadmin_project_path]

files_path = "#{ENV["HOME"]}/files"

if ARGV.length == 0 || (ARGV.length == 1 && ARGV[0] == "help")
  print_usage()
  exit
end

if ARGV.length == 1 && ARGV[0] == "resetonly"
  projects_paths.each { |p| reset_source(p) }
  exit
end

environment = ARGV[0]

if ARGV[1] && ARGV[1] == "reset"
  projects_paths.each { |p| reset_source(p) }
end

#### Logback XML configuration ####

projects_paths.each do |project_path|

  proj = project_path.partition('-').last.downcase

  temp_logback_xml_path = "#{project_path}/resources/logback.temp.xml"
  logback_xml_path = "#{project_path}/resources/logback.xml"

  File.open(temp_logback_xml_path, 'w') do |fo|
    File.foreach(logback_xml_path) do |li|
      li = li.gsub(/(<property name="PREFIX_HOME") value=".*"(\/>)/, "\\1 value=\"/Users/jon.miller/files/logs/#{proj}/\"\\2")
      li = li.gsub(/<!-- Commented for Prod <appender-ref ref="STDOUT"\/>  -->/, "<appender-ref ref=\"STDOUT\"\/>")
      fo.puts(li) unless li.match(/<appender-ref ref="EMAIL"\/>/)
    end
  end

  FileUtils.cp(temp_logback_xml_path, logback_xml_path)
  FileUtils.rm(temp_logback_xml_path)

end

#### Server properties configuration ####

projects_paths.each do |project_path|

  temp_server_props_path = "#{project_path}/WebContent/WEB-INF/conf/spring/properties/server.temp.properties"
  server_props_path = "#{project_path}/WebContent/WEB-INF/conf/spring/properties/server.properties"

  File.open(temp_server_props_path, 'w') do |fo|
    File.foreach(server_props_path) do |li|
      if environment == "test-qa"
        li = li.gsub(/^jdbc\.url=.*$/, "jdbc.url=jdbc:sqlserver://52.1.198.153;databaseName=EACCEPT_TEST")
        li = li.gsub(/^jdbc\.password=.*$/, "jdbc.password=LCXhWf3Di672pEW")
      elsif environment == "test-temp"
        li = li.gsub(/^jdbc\.url=.*$/, "jdbc.url=jdbc:sqlserver://52.1.198.153;databaseName=eACCEPT_Temp1")
        li = li.gsub(/^jdbc\.password=.*$/, "jdbc.password=LCXhWf3Di672pEW")
      elsif environment == "dev"
        li = li.gsub(/^jdbc\.url=.*$/, "jdbc.url=jdbc:sqlserver://NV-DEVSRV3.amcad.com;instanceName=SQL2014;databaseName=eACCEPT_DEV")
        li = li.gsub(/^jdbc\.password=.*$/, "jdbc.password=ecomadmin")
      end
      li = li.gsub(/^jasper\.tempfile\.path=.*$/, "jasper.tempfile.path=/temp")
      li = li.gsub(/localhost(\\)?:(\d)001/) { "localhost#{$1}:#{$2.to_i + 1}001" }
      fo.puts(li)
    end
  end

  FileUtils.cp(temp_server_props_path, server_props_path)
  FileUtils.rm(temp_server_props_path)

end

