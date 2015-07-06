metaworks
=========


# Step 1. Metaworks3 의 기본 프로젝트 구조 다운로드 받기
 
## maven 이 있다면, 다음과 같이 쉽게 기본 프로젝트를 생성할 수 있다

```
mvn archetype:generate -DarchetypeGroupId=org.uengine -DarchetypeArtifactId=metaworks-sample-archetype -DarchetypeVersion=1.1-SNAPSHOT -DarchetypeRepository=https://oss.sonatype.org/content/repositories/snapshots
```
 
#Step 2. 프로젝트 설정

* 이클립스의 경우, 프로젝트를 Maven Project 로 전환하기 위하여 프로젝트명에 우측마우스클릭을 한후, Configure > Convert To Maven Project 해준다. (이때 M2Eclipse 플러그인이 설치되어있어야 한다. 설치방법 - http://blog.doortts.com/59)

* 추가적으로 쉽게 WAS상에서 실행하기 위하여 프로젝트를 Dynamic Java Project 로 전환한다. Configure > Project Facets > Dynamic Web Project 옵션을 추가해준다. (방법: http://www.mkyong.com/java/how-to-convert-java-project-to-web-project-in-eclipse/)

* 프로젝트 소스코드 위치를 정상으로 잡아주기 위하여 프로젝트명에 우측마우스 클릭 > Properties > Java Build Path > Source 에 설정된 기존 패스를 Remove 해주고, src/main/java 를 선택해준다.

* src 폴더의 소스코드들이 정상적으로 컴파일이 되었다면 정확히 설정을 마친것이다. 

#Step 3. 기본 샘플의 정상작동 확인
* 프로젝트 명을 우클릭한후 Run As > Run on Server 를 선택하여 톰캣을 실행한다.
* localhost:8080/metaworks-sample/runner.html?classname=Login 으로 접속한다.
* 기본 샘플 화면이 들어오면 정상이다.
* 화면이 열리지 않으며 deploy 한 포트넘버와 컨텍스트명을 확인한다.

#Step 4.HelloWorld해보기
 
간단한 샘플 애플리케이션 - Hello.java

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
 
#Hello.ejs
```
<h1><%=value.message%></h1>
<%=methods.sayHello.here()%>
```

