package awsLinkMicro;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AwsLinkMicro {
	public static void main(String[] args) throws IOException {
		 
		/*
		 * ���� ���̵� 
		 * 1. aws ���ø�ũ���� segments.gz ������ �ٿ�ε� �޽��ϴ� 
		 * 2. �ش� ���� ������ Ǭ��, �ȿ� ������ txt ���Ϸ� �ٽ� �����մϴ�.
		 * 3.textFileLink �� �ش� txt ������ ��ġ�� ������ �ݴϴ�.
		 * 4.aws �⺻ ��ũ�� ���� �ݴϴ�.
		 * 5. ��ũ������ ������ linkCount �� �����մϴ� (�� : 3 -> 3����)
		 * 6. �ش� ��ũ��Ʈ�� �����մϴ�. �׷��� �ֿܼ� ��ũ�� ǥ�õ˴ϴ�. (�ش� ��ũ������ resultLink �� ���� ����˴ϴ�.)
		   7. �� �ؽ�Ʈ������ �ϳ� ���, �ܼ� ��ü�� ���� �ٿ� �ֱ� �մϴ�.
		   8. ������ ��ũ�� ����  ������ tmux new -t �� ���� �մϴ�.
		 */		 
		AwsLinkMicro awkLinkMicro = new AwsLinkMicro();
		 
		 //String textFileLink = "C://JavaProject/awsLinkPath/crawl-dataCC-MAIN-2020-16.txt";
		 String textFileLink = "C://JavaProject/awsLinkPath/crawl-dataCC-MAIN-2020-24.txt";
		 //String baseLink = "sudo -H -u filadmin aws s3 sync s3://commoncrawl/crawl-data/CC-MAIN-2020-16 /mnt/nas/crawl-data/CC-MAIN-2020-16";
		 String baseLink = "sudo -H -u filadmin aws s3 sync s3://commoncrawl/crawl-data/CC-MAIN-2020-24 /mnt/dataset/crawl-data/CC-MAIN-2020-24";
		 		// 
		 baseLink += " --exclude \"*\" "; 
		 int linkCount = 10;
		 List<String> resultLink = awkLinkMicro.awsLinkMacro(textFileLink,baseLink,linkCount);
    }
	
	public List<String> awsLinkMacro (String textFileLink,String baseLink, int linkCount){
		
		 File file = new File(textFileLink);
	        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
	            String line;
	            List<String> links = new ArrayList<String>();
	            int counter=0;
	            while ((line = br.readLine()) != null) {
	                line = line.substring(line.indexOf("segments"));
	            	links.add(line);
	            	counter++;
	            }   
	            	System.out.println("���Ͽ��� �о�� ���� �� : " + counter);
	            	counter = 0;
	            // listMixer : �ٿ�ε� ��ũ�ÿ�, ��¥�� ���丮���� ���� üũ�ϱ⶧����, ������ ��ũ�� �ȿ� ���丮 ������ �����Ͽ�, �ٿ�ε� ������ ������ �ְ� �Ͽ����ϴ�.
	            links = listMixer(links,linkCount);
	            	System.out.println("������ũ �ͽ��Ѵ��� ��� �� :" + links.size());
	            int eachLinkSize = links.size()/linkCount;
	            // eachLinkSize : ������ ��ũ�� �ּ��� ��� ���丮�� ������ ����
	            int sizeBuffer = links.size()%linkCount;
	            // sizeBuffer : �ּ����� ���丮�� �����ϰ�, �ٸӴ� ��� ���丮����  ������.
	            int startIndex = 0;
	            int endIndex = 0;
	            
	            List<String> newLinks = new ArrayList<String>();
	            //��ũ ���� �ݺ�
	            for(int i=0;i<linkCount;i++) {
	            	String tempLink = "";
	            	if(sizeBuffer>0) {
	            		//sizeBuffer �� �����Ұ��, �ּ� ���丮���� �ϳ��� �� �߰�����, buffer �� �ϳ� ���Դϴ�.
	            		sizeBuffer--;
	            		endIndex += eachLinkSize + 1;
	            	}else {
	            		endIndex += eachLinkSize;
	            		//sizeBuffer �� ���� ���Ұ��, �׳� �ּ� ���丮 ���ڷ�
	            	}
		            for(int j=startIndex; j<endIndex;j++) {
		            	tempLink += " --include \"" + links.get(j) + "*\"";
		            	//������ ���丮�� --include �� �߰� �մϴ�.
	            		counter++;
		            }     
		            startIndex = endIndex;
		            System.out.println("�߰��� ���� ��ũ �� :" + counter);
		            counter =0;
	            	System.out.println(baseLink + tempLink);
	            	newLinks.add(baseLink + tempLink);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		
		
		return null;
	}
	
	
	
	public List<String> listMixer (List<String> list,int linkCount){
		List<String> newList = new ArrayList<String>();
		for(int j = 0;j<linkCount;j++) {	
			for(int i=j;i<list.size();i += linkCount ) {
				newList.add(list.get(i));
			}
		}
		return newList;
	}
	
	
}
