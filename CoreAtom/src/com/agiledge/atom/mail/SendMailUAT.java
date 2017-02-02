package com.agiledge.atom.mail;

public class SendMailUAT implements SendMail {

	private String bccs[];
	private String ccs[];
	private String msg[];
   


	public String[] getBccs() {
		return bccs;
	}




	public void setBccs(String[] bccs) {
		this.bccs = bccs;
	}




	public String[] getCcs() {
		return ccs;
	}




	public void setCcs(String[] ccs) {
		this.ccs = ccs;
	}




	public void send(String destadd, String sub, String dat) {
		System.out.println("address : " + destadd);
		String bccs[]=getBccs();
		String ccs[]=getCcs();
		if(bccs!=null&&bccs.length>0)
		{
			System.out.println("BCCs : ");
			for(String bcc: bccs)
			{
				System.out.print(bcc + "; " );
			}
		}
		if(ccs!=null&&ccs.length>0)
		{
			System.out.println("CCs : ");
			for(String cc: ccs)
			{
				System.out.print(cc + "; " );
			}
		}
		System.out.println("Subject : " + sub);
		System.out.println("Message : " + dat);
	}




	@Override
	public void send_invokedByRun(String address, String subject,
			String mailBody) throws Exception {
		String bccs[]=getBccs();
		String ccs[]=getCcs();
		if(bccs!=null&&bccs.length>0)
		{
			System.out.println("BCCs : ");
			for(String bcc: bccs)
			{
				System.out.print(bcc + "; " );
			}
		}
		if(ccs!=null&&ccs.length>0)
		{
			System.out.println("CCs : ");
			for(String cc: ccs)
			{
				System.out.print(cc + "; " );
			}
		}
		System.out.println("TO : " + address);
		System.out.println("Subject : " + subject);
		System.out.println("Message : " + mailBody);
		
	}
	
	public void send(String destadds[], String sub, String msg[],String cc[]) {
		System.out.println("sub: "+sub);
		for(int i=0;i<destadds.length;i++)
		{
			System.out.println("address : " + destadds[i]);
			System.out.println("ccs : " + cc[i]);
			System.out.println("msg: " + msg[i]);
			
		}
		
		

			}
	public void sendBulkMails(String destadd[], String sub, String dat[],String cc[])
			throws Exception {		
		System.out.println("sub: "+sub);
		for(int i=0;i<destadd.length;i++)
		{
			System.out.println("address : " + destadd[i]);
			System.out.println("ccs : " + cc[i]);
			System.out.println("msg: " + msg[i]);
			
		}
	}

}
