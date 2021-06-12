package ordersSystem.trans;

public class QueryFreqAddressInfo {
	private String freqId;
	private String freqName;
	private String freqPhone;
	private String[] freqAddress;
	private String freqDetailAddress;
	private String freqType;
	
	public String getFreqId() {
		return freqId;
	}
	public void setFreqId(String freqId) {
		this.freqId = freqId;
	}
	public String getFreqName() {
		return freqName;
	}
	public void setFreqName(String freqName) {
		this.freqName = freqName;
	}
	public String getFreqPhone() {
		return freqPhone;
	}
	public void setFreqPhone(String freqPhone) {
		this.freqPhone = freqPhone;
	}
	public String[] getFreqAddress() {
		return freqAddress;
	}
	public void setFreqAddress(String[] freqAddress) {
		this.freqAddress = freqAddress;
	}
	public String getFreqDetailAddress() {
		return freqDetailAddress;
	}
	public void setFreqDetailAddress(String freqDetailAddress) {
		this.freqDetailAddress = freqDetailAddress;
	}

	public String getFreqType() {
		return freqType;
	}

	public void setFreqType(String freqType) {
		this.freqType = freqType;
	}
}
