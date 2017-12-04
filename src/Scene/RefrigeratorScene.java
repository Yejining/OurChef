package Scene;

import java.util.*;
import java.util.List;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import Main.*;
import Scene.*;
import GUI.*;
import Data.*;

public class RefrigeratorScene extends SceneAbst
{
	private static final int MAX_LINES = 6;
	private static final int[][] LINE_BOUNDS = {
			{ 300, 65, 450, 95 },
			{ 300, 215, 450, 95 },
			{ 330, 345, 410, 95 },
			{ 310, 490, 450, 95 },
			{ 330, 620, 430, 95 },
			{ 340, 770, 410, 95 }
	};
	private static final int[] ITEM_SIZE = { 77, 65 };

	// 배경
	private ImageIcon imgBackground;
	private JLabel lblBackground;

	// 뒤로가기 및 홈버튼
	private ImageButton btnGoBack, btnGoHome;

	// 카트
	private ImageButton btnCart;

	// 검색
	private JTextField txtInput;
	private ImageButton btnSearch;

	// 각 라인의 패널
	private JPanel[] pnlLine;
	// 각 라인의 스크롤 페인
	private JScrollPane[] pnlLineScroll;
	
	// 재료 버튼
	private HashMap<JButton, Ingredient> ingButtonMap;
	
	// 애니메이션
	private Animation animCartMove;

	// 이벤트
	private RefrigeratorListener refL;
	private IngredientButtonListener ingButtonL;

	public void onShow() {
		// 재료 정보 가져오기
		List<Ingredient> ingredients = Main.getIngredients();
		
		refL = new RefrigeratorListener();
		ingButtonL = new IngredientButtonListener();

		pnlLine = new JPanel[MAX_LINES];
		pnlLineScroll = new JScrollPane[MAX_LINES];
		for (int i = 0; i < MAX_LINES; i++) {
			// 라인 패널 생성
			pnlLine[i] = new JPanel();
			pnlLine[i].setLayout(new FlowLayout());
			pnlLine[i].setOpaque(false);
			
			// 라인 스크롤 페인 생성
			pnlLineScroll[i] = new JScrollPane(pnlLine[i]);
			pnlLineScroll[i].setBounds(LINE_BOUNDS[i][0], LINE_BOUNDS[i][1], LINE_BOUNDS[i][2], LINE_BOUNDS[i][3]);
			pnlLineScroll[i].getViewport().setOpaque(false);
			pnlLineScroll[i].setOpaque(false);
			pnlLineScroll[i].setBorder(null);
			add(pnlLineScroll[i]);
		}
		
		// 재료 버튼 추가
		ingButtonMap = new HashMap<JButton, Ingredient>();
		for (Ingredient ing : ingredients) {
			// 아이콘
			ImageIcon icon = new ImageIcon(ing.getIcon());
			Image image = icon.getImage();			
			image = image.getScaledInstance((int)((float)icon.getIconWidth()/icon.getIconHeight()*ITEM_SIZE[1]), ITEM_SIZE[1], java.awt.Image.SCALE_SMOOTH);
			icon = new ImageIcon(image);

			// 재료 버튼
			JButton btn = new JButton();
			btn.setIcon(icon);
			btn.setPreferredSize(new Dimension(ITEM_SIZE[0], ITEM_SIZE[1]));
			btn.setContentAreaFilled(false); // 버튼 바탕색 제거
			btn.setBorderPainted(false); // 버튼 테두리 제거
			btn.addActionListener(ingButtonL);
			pnlLine[ing.getType()].add(btn);
			
			ingButtonMap.put(btn, ing);
		}

		// CartScene으로 넘어가는 버튼 추가
		btnCart = new ImageButton("images/cart.png", 900, 350);
		btnCart.addActionListener(refL);
		add(btnCart);

		// 검색
		txtInput = new JTextField();
		txtInput.setBounds(950, 70, 430, 55);
		txtInput.addActionListener(refL);
		add(txtInput);
		btnSearch = new ImageButton("images/searchIcon.png", "images/searchIcon_h.png", 1390, 70);
		btnSearch.addActionListener(refL);
		add(btnSearch);

		// 뒤로가기 버튼 및 홈버튼
		btnGoBack = new ImageButton("images/goBack.png", 7, 810);
		btnGoBack.setLayout(null);
		btnGoBack.setContentAreaFilled(false);
		btnGoBack.setBorderPainted(false);
		btnGoBack.addActionListener(refL);
		add(btnGoBack);

		btnGoHome = new ImageButton("images/goHome.png", 20, 25);
		btnGoHome.setLayout(null);
		btnGoHome.setContentAreaFilled(false);
		btnGoHome.setBorderPainted(false);
		btnGoHome.addActionListener(refL);
		add(btnGoHome);
		
		// 배경
		imgBackground = new ImageIcon("images/refBackground2.png");
		lblBackground = new JLabel();
		lblBackground.setIcon(imgBackground);
		lblBackground.setBounds(0, 0, 1600, 900);
		add(lblBackground);
	}

	public void onHide() {

	}

	private class RefrigeratorListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event) {
			Object obj = event.getSource();

			if (obj == btnCart) { // 카트 버튼 클릭
				animCartMove = new Animation(btnCart, false, new MoveListener());
				animCartMove.move(1500, 250, 500);
			} else if (obj == btnGoBack) { // 뒤로 버튼 클릭
				Main.switchScene(new IntroScene());
			} else if (obj == btnGoHome) { // 홈 버튼 클릭
				Main.switchScene(new IntroScene());
			} else if (obj == txtInput || obj == btnSearch) {

			}
		}
	}
	
	private class IngredientButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event) {
			Object obj = event.getSource();
			List<Ingredient> cart = Main.getCart(); // 카트 리스트 객체
			Ingredient ing = ingButtonMap.get(obj); // 선택한 재료
			
			if (!cart.contains(ing)) { // 선택한 재료를 카트에 추가
				cart.add(ing);
				System.out.println("카트에 추가됨: " + ing.getName());
			} else {
				cart.remove(ing);
				System.out.println("카트에서 제거됨: " + ing.getName());
			}
		}
	}
	
	private class MoveListener implements AnimationListener
	{
		public void onCompleted() {
			Main.switchScene(new CartScene());
		}
	}
}