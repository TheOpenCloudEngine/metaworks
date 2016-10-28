metaworks
=========


# Metaworks3 의 기본 프로젝트 구조 다운로드 받기
 
## maven 이 있다면, 다음과 같이 쉽게 기본 프로젝트를 생성할 수 있다

```
mvn archetype:generate \
-DarchetypeGroupId=org.uengine \
-DarchetypeArtifactId=metaworks-sample-archetype \
-DarchetypeVersion=1.1-SNAPSHOT \
-DarchetypeRepository=https://oss.sonatype.org/content/repositories/snapshots
```

for windows:
```
mvn archetype:generate -DarchetypeGroupId=org.uengine -DarchetypeArtifactId=metaworks-sample-archetype -DarchetypeVersion=1.1-SNAPSHOT -DarchetypeRepository=https://oss.sonatype.org/content/repositories/snapshots
```
## 생성된 기본 maven 프로젝트에서 빌드후 바로 실행해보기

톰캣 서버를 설치 없이 바로 테스트 하시려면 아래의 Tomcat plugin 설정을 pom.xml의 plugins 구역에 추가해주셔야 합니다:

```
<plugin>
     <groupId>org.apache.tomcat.maven</groupId>
     <artifactId>tomcat7-maven-plugin</artifactId>
     <version>2.2</version>
     <configuration>
       <server>local</server>
       <port>8080</port>
     </configuration>
 </plugin>
```

위와 같이 편집을 완료한 후에 다음과 같이 패키징과 실행이 가능합니다:

```
mvn package
mvn tomcat7:run-war
```

접속 테스트를 하실때는 생성할때의 artifactId 를 참고하여 다음의 주소로 접속이 가능합니다:

```
localhost:8080/<artifactId>
```

# [인텔리J에서 메이븐 명령 실행시] maven.multiModuleProjectDirectory 오류가 나시면
File > Settings > Maven >> Runner 에서
VMOptions에 다음을 입력
```
-Dmaven.multiModuleProjectDirectory=C:\apache-maven-3.3.1-bin\apache-maven-3.3.1
```


## 기본 샘플의 정상작동 확인
* localhost:8080/metaworks-sample/runner.html?classname=Login 으로 접속한다.
* 기본 샘플 화면이 들어오면 정상이다.
* 화면이 열리지 않으며 deploy 한 포트넘버와 컨텍스트명을 확인한다.

## maven 이 없고, 이클립스만 사용할 수 있다면, 다음 위치에서 이클립스 프로젝트를 다운받는다:

https://github.com/TheOpenCloudEngine/metaworks/tree/master/doc/mw3-eclipse-sample-project.zip

 
# IDE 에서 프로젝트 설정

* 이클립스의 경우, 프로젝트를 Maven Project 로 전환하기 위하여 프로젝트명에 우측마우스클릭을 한후, Configure > Convert To Maven Project 해준다. (이때 M2Eclipse 플러그인이 설치되어있어야 한다. 설치방법 - http://blog.doortts.com/59)
* 추가적으로 쉽게 WAS상에서 실행하기 위하여 프로젝트를 Dynamic Java Project 로 전환한다. Configure > Project Facets > Dynamic Web Project 옵션을 추가해준다. (방법: http://www.mkyong.com/java/how-to-convert-java-project-to-web-project-in-eclipse/)
* 프로젝트 소스코드 위치를 정상으로 잡아주기 위하여 프로젝트명에 우측마우스 클릭 > Properties > Java Build Path > Source 에 설정된 기존 패스를 Remove 해주고, src/main/java 를 선택해준다.
* src 폴더의 소스코드들이 정상적으로 컴파일이 되었다면 정확히 설정을 마친것이다. 
* [이클립스에서] 프로젝트 명을 우클릭한후 Run As > Run on Server 를 선택하여 톰캣을 실행한다.

# HelloWorld해보기
 
## 간단한 샘플 애플리케이션 - Hello.java

```
package metaworks.test;
 
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.Popup;
 
 
public class Hello{
 
    String name;
        public String getName() {
            return name;
        }
 
        public void setName(String name) {
            this.name = name;
        }
 
         
    String message;
        public String getMessage() {
            return message;
        }
 
        public void setMessage(String message) {
            this.message = message;
        }
 
 
    public Hello(){
        setName("Jinyoung Jang");
    }
 
    @ServiceMethod
    public void sayHello(){
        setMessage("Hello, " + getName());
    }
 
    @ServiceMethod(inContextMenu=true, target=ServiceMethodContext.TARGET_POPUP)
    public Popup sayGoodbye(){
        setMessage("Goodbye, " + getName());
        return new Popup(this);
    }
 
}
```
 
## Hello.ejs
```
<h1><%=value.message%></h1>
<%=methods.sayHello.here()%>
```

## ejs 내에서 사용할 수 있는 reserved variables
```
var contextValues = {
							value				: object,
							realValue			: org_object,
							objectTypeName		: objectTypeName,
							targetDiv			: targetDiv, 
							objectMetadata		: (objectTypeName && objectTypeName.length > 0 ? this.getMetadata(objectTypeName) : null), 
							mw3					: this, 
							objectId			: objectId, 
							fields				: (objectRef ? objectRef.fields  : null),
							resources			: (objectRef ? objectRef.fields  : null), //TODO: later should be sent only with resources
							methods				: (objectRef ? objectRef.methods : null),
							descriptor			: descriptor,
							editFunction		: editFunction,
							options				: options								
						};
```

# 동적으로 변경된 메타데이터 반영하기

1. JRebel 을 사용
2. http://localhost:8080/runner.html?classname=org.metaworks.Admin 를 호출하여 변경하고자 하는 클래스명을 넣은 후, refreshMetadata 버튼을 클릭함. 모든 클래스를 재반영하려면, * 를 입력함.

# 관련 문서
1. [Metaworks Wiki Documentation](https://github.com/TheOpenCloudEngine/metaworks/wiki)

# Tutorial Videos
* press HD when you see the movies

1. Install and build:  https://www.facebook.com/1401720840/videos/o.219096314846012/10205019275227194/?type=2&theater
2. First Metaworks Application:  https://www.facebook.com/1401720840/videos/o.219096314846012/10205021538603777/?type=2&theater
3. Context, Hidden, Available, and ejs file:    https://www.facebook.com/1401720840/videos/o.219096314846012/10205021546243968/?type=2&theater
4. ServiceMethod, target, popup:    https://www.facebook.com/1401720840/videos/o.219096314846012/10205021632086114/?type=2&theater
5. Developing Custom widgets (ejs.js) : https://www.facebook.com/1401720840/videos/o.219096314846012/10205021633926160/?type=2&theater
6. Database Application:  
https://www.facebook.com/1401720840/videos/o.219096314846012/10205021646446473/?type=2&theater&notif_t=video_processed
