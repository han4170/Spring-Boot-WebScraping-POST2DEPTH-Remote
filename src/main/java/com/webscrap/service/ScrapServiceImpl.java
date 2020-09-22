package com.webscrap.service;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;		
import java.util.Set;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.Platform;

import com.webscrap.dto.WebscrapingDto;
import com.webscrap.dto.WebscrapingInputDto;
import com.webscrap.dto.WebscrapingListDto;
import com.webscrap.dto.WebscrapingItemListDto;

@Service
public class ScrapServiceImpl implements ScrapService{
	
	@Override
	public WebscrapingDto selectWebListPost(WebscrapingInputDto WebscrapingInput) {
		//ChromeDriver driver = null;
		WebDriver driver = null;		
		WebscrapingDto WebscrapingData = new WebscrapingDto();
		List<WebscrapingListDto> WebscrapingListDataAll = new ArrayList<WebscrapingListDto>();
		List<WebscrapingItemListDto> WebscrapingItemListDataAll = new ArrayList<WebscrapingItemListDto>();
		
		try {
            //************* 
			// 서비시 시작 시간
			//*************
			SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
			long reqTime = System.currentTimeMillis();
			String current_start = dayTime.format(new Date(reqTime));
			//*************
			// post input
			//*************
			System.out.println("패스워드  : " + WebscrapingInput.getPasswd());
			System.out.println("기간 From  : " + WebscrapingInput.getPeroidFrom());
			System.out.println("기간 To  : " + WebscrapingInput.getPeroidTo());

			String period_from = WebscrapingInput.getPeroidFrom();
			String period_to = WebscrapingInput.getPeroidTo();
			String passwd = WebscrapingInput.getPasswd();

			//*******************
			// WebDriver
			//*******************
			/*
			// WebDriver 경로 설정
			System.setProperty("webdriver.chrome.driver", "D:\\hanu_work\\RPA\\selenium\\chromedriver_win32_85\\chromedriver.exe");

			// WebDriver 옵션 설정
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--start-maximized"); // 전체화면으로 실행
			options.addArguments("--disable-popup-blocking"); // 팝업 무시
			options.addArguments("--disable-default-apps"); // 기본앱 사용안함
			//options.addArguments("headless"); // 브라우저 오픈하지 않음

			// WebDriver 객체 생성
			// ChromeDriver driver = new ChromeDriver( options );
			driver = new ChromeDriver(options);
            */
		    DesiredCapabilities caps = new DesiredCapabilities();
		    caps.setCapability("browserName", "chrome");
		    caps.setCapability("platform", Platform.WINDOWS);
		    driver = new RemoteWebDriver(new URL("http://10.0.0.23:4444/wd/hub"), caps);
		    driver.manage().window().maximize();			
			//*******************
			// 국세청 홈텍스 Scraping
			// - 현금영수증 - 사용내역 소득공제 누계조회
			//*******************
			//**********************
			//1. 국세청 홈텍스 URL Open
			//**********************
			driver.get("https://www.hometax.go.kr/websquare/websquare.html?w2xPath=/ui/pp/index.xml");

			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			// 5초간 중지시킨다.(단위 : 밀리세컨드)
			Thread.sleep(5000);
			//**********************
			//2.  로그인 선택
			//**********************
			WebElement element = driver.findElement(By.cssSelector("#group88615548"));
			element.click();
			System.out.println("Clicked on : " + element);
			// 5초간 중지시킨다.(단위 : 밀리세컨드)
			Thread.sleep(5000);
			// 국세청 홈텍스 POP-UP Windows Close 
			String HomeMainWindow = null;
			Set<String> PopUpWin = null;
			Iterator<String> PopUpWinI = null;
			HomeMainWindow = driver.getWindowHandle();
			// 새로 열린 모든 창을 처리합니다.
			PopUpWin = driver.getWindowHandles();
			PopUpWinI = PopUpWin.iterator();

			while (PopUpWinI.hasNext()) {
				String PopUpWindow = PopUpWinI.next();

				if (!HomeMainWindow.equalsIgnoreCase(PopUpWindow)) {

					// 하위 창으로 전환
					driver.switchTo().window(PopUpWindow);
					// 자식 창 닫기.
					driver.close();
				}
			}
			// 부모 윈도우, 즉 메인 윈도우로 전환.
			driver.switchTo().window(HomeMainWindow);			
			//**********************
			//3. 로그인 화면
			//**********************
            //3.1 공인인증서 로그인이 있는 프레임 이동
			driver.switchTo().frame("txppIframe");
			//3.2 공인인증서 로그인 선택
			WebElement element1 = driver.findElement(By.cssSelector("#trigger38"));
			element1.click();
			System.out.println("Clicked on : " + element1);
			// 10초간 중지시킨다.(단위 : 밀리세컨드)
			Thread.sleep(10000);

			//3.3 공인인증서 - 프레임 이동
			driver.switchTo().frame("dscert");

			//3.4 공인인증서 - 하드디스크 이동식 선택
			//WebElement element2 = driver.findElement(By.xpath("/html[1]/body[1]/div[5]/div[2]/div[1]/div[1]/div[4]/div[1]/form[1]/div[1]/div[1]/ul[1]/li[2]/a[1]"));
			WebElement element2 = driver.findElement(By.cssSelector("#stg_hdd"));
			element2.click();
			System.out.println("Clicked on : " + element2);
			//3.5 공인인증서 - 로컬 디스크 (C) 선택
			Thread.sleep(2000);
			WebElement element3 = driver.findElement(By.xpath("//a[text()='로컬 디스크 (C)']"));
			element3.click();
			System.out.println("Clicked on : " + element3);

			//3.6 공인인증서 - 인증서비밀번호 입력
			WebElement element4 = driver.findElement(By.id("input_cert_pw"));
			// element4.sendKeys("alghkoo3**");
			// password input 설정
			element4.sendKeys(passwd);

			//3.7 공인 인증서 - 확인버튼 선택 , 로그인 완료
			WebElement element5 = driver.findElement(By.id("btn_confirm_iframe"));
			element5.click();
			System.out.println("Clicked on : " + element5);

			// 3.8 로그인 완료 후 POP-UP Windows Close
			Thread.sleep(3000);
			PopUpWin = driver.getWindowHandles();
			PopUpWinI = PopUpWin.iterator();

			while (PopUpWinI.hasNext()) {
				String PopUpWindow = PopUpWinI.next();

				if (!HomeMainWindow.equalsIgnoreCase(PopUpWindow)) {

					// 하위 창으로 전환
					driver.switchTo().window(PopUpWindow);
					// 자식 창 닫기.
					driver.close();
				}
			}
			// 부모 윈도우, 즉 메인 윈도우로 전환.
			driver.switchTo().window(HomeMainWindow);			
			//**********************
			//4. 조회 메뉴 선택
			//**********************
			Thread.sleep(1000);
			// WebElement element6 =
			// driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/div/div[1]/div[2]/div/ul/li[1]/a"));
			WebElement element6 = driver.findElement(By.id("group1300"));
			element6.click();
			System.out.println("Clicked on : " + element6);
			//4.1 현금영수증 선택
			Thread.sleep(2000);
			driver.switchTo().frame("txppIframe");
			WebElement element7 = driver.findElement(By.id("sub_a_0105010000"));
			element7.click();
			System.out.println("Clicked on : " + element7);
			//4.2 현금영수증 - 사용내역 소득공제 조회 클릭
			//WebElement element8 = driver.findElement(By.id("sub_a_0105010100"));
			//element8.click();
			//System.out.println("Clicked on : " + element8);
			//4.3 현금영수증 - 사용내역 소득공제 누계조회 선택
			Thread.sleep(1000);
			WebElement element8 = driver.findElement(By.id("sub_a_0105010200"));
			element8.click();
		
			Thread.sleep(2000);
			//***********************************
			//5. 현금영수증 - 사용내역 소득공제 누계조회 화면
			//***********************************
			//5.1 현금영수증 - 사용내역 소득공제 누계조회 - 조회년도 선택
			//driver.switchTo().frame("txppIframe");
			Select dropdown = new Select(driver.findElement(By.id("selectTrsYr")));
			//dropdown.selectByVisibleText("2019년");
			dropdown.selectByVisibleText(period_from);
			
			Thread.sleep(1000);
			//5.2 현금영수증 - 사용내역 소득공제 누계조회 - 조회하기 버튼 선택
			WebElement element10 = driver.findElement(By.id("trigger1"));
			element10.click();
			System.out.println("Clicked on : " + element10);
			Thread.sleep(1000);

			//5.3  현금영수증 - 사용내역 소득공제 누계조회 결과 : Table Data Scrap
			// Table Body 찾기 : grid_body_tbody
			WebElement mytable_body = driver.findElement(By.id("grid_body_tbody"));
			// /html/body/div[1]/div[3]/div[4]/div/div[2]/div/table
			System.out.println("mytable_body on" + mytable_body);
			
			//5.3.1 테이블의 row 찾기 : tr
			List<WebElement> rows_table_body = mytable_body.findElements(By.tagName("tr"));
			//5.3.2 테이블의 row 수를 계산
			int rows_count_body = rows_table_body.size();
			System.out.println("rows_count_body " + rows_count_body);

			// 5.3.3 테이블의 루프는 table의 마지막 row까지 실행
			//  row별로 column 수를 동적으로 찾아서 반복하여 데이터 추출 
			 if( rows_count_body > 0) { // row 가 있는 경우
					for (int row = 0; row < rows_count_body; row++) {
						// 테이블 row의 column 찾기 : td
						List<WebElement> Columns_row_body = rows_table_body.get(row).findElements(By.tagName("td"));
						// column 수
						int columns_count_body = Columns_row_body.size();
						System.out.println("Number of cells In Row " + row + " are " + columns_count_body);
						// column 전체 결과 array 저장 1차원: 선언
						String[] results_array = new String[columns_count_body];

						for (int column = 0; column < columns_count_body; column++) {
							// column의  텍스트를 검색
							String celtext_body = Columns_row_body.get(column).getText();
							if (celtext_body.trim().length() > 0) {
								System.out.println("Cell Value of row number " + row + " and column number " + column + " Is " + celtext_body);
								// colum data --> results_array
								results_array[column] = celtext_body;
							}
						}

						System.out.println("-------------------------------------------------- ");

						if ( row < rows_count_body-1 ) { // 월별 데이터 
							// 첫번째column 값이 존재하면  전체 column 값이 존재
							if (  results_array[0] != null) {
								// WebscrapingListData 선언 :  월별 데이터 
								WebscrapingListDto WebscrapingListData = new WebscrapingListDto();

								for (int i = 0; i < results_array.length; i++) {
									System.out.println("row number : " + row + " results_array : " + results_array[i]);
									if (i == 0) {
										WebscrapingListData.setUsed_Year(results_array[i]); //사용년월
									} else if (i == 1) {
										WebscrapingListData.setUsed_Count(results_array[i]); //사용건수
									} else if (i == 2) {
										WebscrapingListData.setUsed_Amount(results_array[i]); //사용금액
									} else if (i == 3) {
										WebscrapingListData.setDeduction_SubjectAmount(results_array[i]); //공제제외대상금액
									} else if (i == 5) {
										WebscrapingListData.setDeduction_Amount(results_array[i]); //공제대상금액
									}
								}

								// WebscrapingListData를 WebscrapingListDataAll에 넣기
								WebscrapingListDataAll.add(WebscrapingListData);
							}
							

						} else { // 총합계
							for (int i = 0; i < results_array.length; i++) {
								System.out.println("row number : " + row + " results_array : " + results_array[i]);
								if (i == 1) {
									WebscrapingData.setTotal_count(results_array[i]); //총합계 사용건수
								} else if (i == 2) {
									WebscrapingData.setTotal_amount(results_array[i]); //총합계 사용금액
								} else if (i == 3) {
									WebscrapingData.setTotal_deduction_subjectamount(results_array[i]); //총합계 공제제외대상금액
								} else if (i == 5) {
									WebscrapingData.setTotal_deduction_amount(results_array[i]); //총합계 공제대상금액
								}
							}							
							
						}

					}
			 }

			//5.4  현금영수증 - 사용내역 소득공제 누계조회 결과 : 월별 상세 데이터 
            //5.4.1  현금영수증 - 사용내역 소득공제 누계조회 결과 - table 데이터 내의 link 찾기
	        List<WebElement> links=mytable_body.findElements(By.tagName("a"));
	        System.out.println("no of links:" +links.size());
	        String MainWindow=driver.getWindowHandle();
	        System.out.println("MainWindow : " + MainWindow);
	        
	        //5.4.2  현금영수증 - 사용내역 소득공제 누계조회 결과  link 클릭 
	        //      POP-UP 화면 오픈 - 현금영수증 사용내역 화면  
	        //      페이지 link 클릭 : 페이지 마다 Table 데이터 추출
			for (int i = 0; i < links.size(); i++) { // 월별 전체 link
			//for (int i = 0; i < 1; i++) { // link 1개 - test용
				if (!(links.get(i).getText().isEmpty())) {
					// 월별 link 클릭 
					System.out.println("link : " + links.get(i).getText());
					String Used_Year = links.get(i).getText();
					links.get(i).click();
					Thread.sleep(2000);
					// windows 가져오기
					Set<String> s1 = driver.getWindowHandles();
					Iterator<String> i1 = s1.iterator();
					while (i1.hasNext()) {
						String ChildWindow = i1.next();
						
						if (!MainWindow.equalsIgnoreCase(ChildWindow)) { // 신규 팝업화면인 경우 - 현금영수증 사용내역 화면
							System.out.println("ChildWindow : " + ChildWindow); 
							// 하위 창으로 전환 - 현금영수증 사용내역 화면
							driver.switchTo().window(ChildWindow);
							String MainWindow1=driver.getWindowHandle();
							System.out.println("MainWindow1 : " + MainWindow1);
							System.out.println("ChildWindow page title : "+driver.getTitle() );

							//현금영수증 사용내역 화면 내의  page link 찾기 및 배열에 저장
							List<WebElement> mytable_body_page = driver.findElements(By.tagName("a"));
							int mytable_body_page_count = mytable_body_page.size();
							System.out.println("no of mytable_body_page : " +(mytable_body_page_count-4));
							String[] page =new String[mytable_body_page_count-4];
							for (int j = 0; j < mytable_body_page_count; j++) {
								if ( j > 2 && j < mytable_body_page_count-1)  {
									page[j-3] = "/html/body/div[2]/div[3]/div/div[3]/div/div/div[1]/ul/li["+j+"]/a";
									System.out.println("page xpath : " +page[j-3]);
								}
							}
							// 현금영수증 사용내역 화면 내의  page link마다 클릭 후에  table 데이터 추출
							for (int K = 0; K < page.length; K++) {
								// page link 클릭
								WebElement element12 = driver.findElement(By.xpath(page[K]));
								element12.click();
								System.out.println("Clicked on : " + element12);
								// Table 찾기 : grid1_body_table
								WebElement mytable_body1 = driver.findElement(By.id("grid1_body_table"));
								// /html/body/div[1]/div[3]/div[4]/div/div[2]/div/table
								System.out.println("mytable_body1 on" + mytable_body1);
								// 테이블의 row 찾기 : tr
								List<WebElement> rows_table_body1 = mytable_body1.findElements(By.tagName("tr"));
								// 테이블의 row 수를 계산
								int rows_count_body1 = rows_table_body1.size();
								System.out.println("rows_count_body1 " + rows_count_body1);

								for (int row = 0; row < rows_count_body1; row++) {
									// 테이블 row의 column 찾기 : td
									List<WebElement> Columns_row_body1 = rows_table_body1.get(row).findElements(By.tagName("td"));
									// 테이블의 row의 columns 수를 계산
									int columns_count_body1 = Columns_row_body1.size();
									System.out.println("Number of cells In Row " + row + " are " + columns_count_body1);
									// 결과 array 저장 1차원: 선언
									String[] results_detail_array = new String[columns_count_body1];
                                    if( columns_count_body1 > 0) {
    									for (int column = 0; column < columns_count_body1; column++) {
    										// column의  텍스트를 검색.
    										String celtext_body = Columns_row_body1.get(column).getText();
    										if (celtext_body.trim().length() > 0) {
    											// // colum data --> results_detail_array
        										System.out.println("Cell Value of row number " + row + " and column number "
        												+ column + " Is " + celtext_body);
        										results_detail_array[column] = celtext_body;
    										} 

    									}
                                   	
                                    }
                    				System.out.println("-------------------------------------------------- ");
                                    // column count > 0 and 첫번째column 값이 존재하면  전체 column 값이 존재
                    				if (  columns_count_body1 > 0 && results_detail_array[0] != null) {
                        				// WebscrapingItemListData 선언 : 현금영수증 사용내역 상세 데이터
                        				WebscrapingItemListDto WebscrapingItemListData = new WebscrapingItemListDto();

                        				for (int l = 0; l < results_detail_array.length; l++) {
                        					System.out.println("row number : " + row + " results_detail_array : " + results_detail_array[l]);
                        					if (l == 0) {
                        						WebscrapingItemListData.setTrading_Date(results_detail_array[l]); //거래일시
                        					} else if (l == 1) {
                        						WebscrapingItemListData.setStore_Name(results_detail_array[l]); //가맹점명
                        					} else if (l == 2) {
                        						WebscrapingItemListData.setAmount_Used(results_detail_array[l]); //사용금액
                        					} else if (l == 4) {
                        						WebscrapingItemListData.setIssued_Method(results_detail_array[l]); //발급수단
                        					} else if (l == 5) {
                        						WebscrapingItemListData.setTrading_Method(results_detail_array[l]); //거래구분
                        					} else if (l == 6) {
                        						WebscrapingItemListData.setDeduction_Status(results_detail_array[l]); //공제여부
                        					} else if (l == 7) {
                        						WebscrapingItemListData.setIssued_classifi(results_detail_array[l]); //발행구분
                        					}
                        				}
                           				WebscrapingItemListData.setUsed_Year(Used_Year);  //사용년월
                        				
                        				// WebscrapingItemListData를 WebscrapingItemListDataAll list에 넣기 : 현금영수증 사용내역 상세 데이터
                        				WebscrapingItemListDataAll.add(WebscrapingItemListData);
                    				}

								}

							}

						}
						
					}

				}
				// 자식 창 닫기 : 현금영수증 사용내역 상세 POP-UP 화면 닫기
				driver.close();
				//부모 윈도우, 즉 메인 윈도우로 전환. 현금영수증 - 사용내역 소득공제 누계조회 결과 화면으로 전환
		        driver.switchTo().window(MainWindow);
				// 웹페이지 소스 출력
		        System.out.println("MainWindow page titme : "+driver.getTitle() );
		        //사용내역 소득공제 누계조회 결과 화면의 Table Data 프레임으로 이동
		        driver.switchTo().frame("txppIframe");
				
			}
			
	        // 부모 윈도우, 즉 메인 윈도우로 전환.
			// 위 로직 에서 전환하고 끝나므로 여기서 전환은 제외
	        //driver.switchTo().window(MainWindow);
	        //driver.switchTo().frame("txppIframe");
	        //Thread.sleep(2000);
	        //MainWindow=driver.getWindowHandle();
	        //System.out.println("MainWindow : " + MainWindow);
	        //System.out.println("MainWindow page titme : "+driver.getTitle() );

			// 메인 윈도우 전환 및 Frame 전환 확인용  : 사용내역 소득공제 누계조회 결과 화면
			//WebElement element11 = driver.findElement(By.xpath("/html/body/div[1]/div[3]/div[3]/div[2]/div/div[2]/div/table/tbody/tr[2]/td[1]/nobr/a"));
			//element11.click();
			//System.out.println("Clicked on : " + element11);
			// 윈도우 전환 및 Frame 전환 확인용

			//***********************************
			//6. 전체 조회 결과 데이터 설정 
			//***********************************			
			// WebscrapingData 전체 데이터 설정
			WebscrapingData.setErrMsg("정상");
			WebscrapingData.setErrYn("N");
			WebscrapingData.setList(WebscrapingListDataAll);
			WebscrapingData.setItemList(WebscrapingItemListDataAll);
			// Webscraping 설정
			// Webscraping.add(WebscrapingData);
			// return scrapMapper.selectWebList();

			//***********************************
			//7. driver close and quit
			//***********************************	
			//WebDriver close 
			driver.close();
			// 2초 후에 WebDriver quit
			Thread.sleep(2000);
			driver.quit();

			//***********************************
			// 기타 : 서비스 시작/종료시간
			//***********************************
			long resTime = System.currentTimeMillis();
			String current_end = dayTime.format(new Date(resTime));
			double elapsed_time = (resTime - reqTime) / 1000.000;
			System.out.println("start_time : " + current_start);
			System.out.println("end_time   : " + current_end);
			System.out.println("end_time - start_time : " + elapsed_time);
			

		} catch (Exception e) {
			driver.close();
			driver.quit();
			WebscrapingData.setErrMsg(e.getMessage());
			WebscrapingData.setErrYn("Y");
			System.out.println("Exception WebscrapingData : " +	WebscrapingData.toString());
			System.out.println("Exception : " + e.getMessage());
		}
		return WebscrapingData;
	}
}
