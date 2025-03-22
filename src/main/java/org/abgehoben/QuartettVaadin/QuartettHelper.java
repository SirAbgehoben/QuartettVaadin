package org.abgehoben.QuartettVaadin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class QuartettHelper {

    public static Div createPlayerCard(QuartettSession quartettSession, Player currentPlayer, Player opponentPlayer, Card card, String role) {
        Div PlayerCard = new Div();
        PlayerCard.getStyle().set("border", "2px solid hsla(214, 64%, 82%, 0.23)");
        PlayerCard.getStyle().set("border-radius", "10px");
        PlayerCard.getStyle().set("padding", "10px");
        PlayerCard.getStyle().set("margin", "10px");
        PlayerCard.getStyle().set("background-color", "hsla(214, 10%, 0%, 0.1)");
        PlayerCard.getStyle().set("box-shadow", "0 4px 6px rgba(0, 0, 0, 0.15)");
        PlayerCard.getStyle().set("backdrop-filter", "blur(40px)");
        PlayerCard.setWidth("300px");
        PlayerCard.setHeight("450px");

        HorizontalLayout NameAndRole = CreateNameAndRole(currentPlayer, role);
        PlayerCard.add(NameAndRole);

        Div CardImage = CreateQuartettCardImage(card);
        PlayerCard.add(CardImage); //update

        Span CardName = CreateCardName(card);
        PlayerCard.add(CardName);

        if (role.equals("You")) {
            VerticalLayout playerButtons = createPlayerButtons(quartettSession, currentPlayer, opponentPlayer, card); //update
            PlayerCard.add(playerButtons);
        } else {
            VerticalLayout attributesLayout = createPlayerAttributesSpan(card); //update
            PlayerCard.add(attributesLayout);
        }

        Span CardsLeft = CardsLeft(currentPlayer);
        PlayerCard.add(CardsLeft);

        return PlayerCard;
    }


    public static Span CardsLeft(Player player) {
        Span CardsLeft = new Span("Cards Left: " + player.getCardsLeft());

        double ratio = player.getCardsLeft() / 16.0;
        int hue = (int) (ratio * 120);
        String color = "hsla(" + hue + ", 90%, 55%, 0.13)";

        CardsLeft.getStyle().set("font-weight", "bold")
                .set("display", "block")
                .set("text-align", "left")
                .setPaddingLeft("8px")
                .set("color", "hsla(214, 87%, 92%, 0.8)")
                .setBackground("linear-gradient(to left, rgba(255,0,0,0), " + color + ")")
                .set("padding", "2px 8px")
                .set("border-radius", "4px");
        return CardsLeft;
    }

    public static VerticalLayout createPlayerAttributesSpan(Card card) {
        VerticalLayout attributesLayout = new VerticalLayout();
        for (String attribute : card.getAttributes().keySet()) {
            Div attributeDiv = CreateAttributeDiv(card, attribute);
            attributesLayout.add(attributeDiv);
        }
        attributesLayout.setSpacing(false);
        attributesLayout.setPadding(false);
        attributesLayout.getStyle().set("gap", "8px");
        attributesLayout.getStyle().setPaddingTop("4px");
        attributesLayout.getStyle().setPaddingBottom("4px").setHeight("152px");
        return attributesLayout;
    }

    public static Div CreateAttributeDiv(Card card, String attribute) {

        HorizontalLayout attributeContent = new HorizontalLayout();
        attributeContent.setWidthFull();
        attributeContent.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        attributeContent.setAlignItems(FlexComponent.Alignment.CENTER);
        attributeContent.getStyle().set("padding", "0 4px");

        Span attributeName = new Span(attribute + ": ");
        attributeName.getStyle().set("color", "white");
        attributeName.getStyle().set("text-align", "left");
        attributeName.setWidth("50%");

        Span attributeValue = new Span(card.getAttributes().get(attribute).toString());
        attributeValue.getStyle().set("color", "white");
        attributeValue.getStyle().set("text-align", "right");
        attributeValue.setWidth("50%");

        Div attributes = new Div(attributeContent);
        attributes.setWidthFull();
        attributes.getStyle().set("color", "white");
        attributes.getStyle().set("background-color", "hsla(214, 10%, 0%, 0.1)");
        attributes.getStyle().set("backdrop-filter", "blur(20px)");
        attributes.getStyle().setBorder("0.5px solid rgba(40, 44, 26, 0.3)");
        attributes.getStyle().setBoxShadow("0 4px 6px rgba(0, 0, 0, 0.1)");
        attributes.getStyle().setHeight("29px");
        attributeContent.add(attributeName, attributeValue);
        attributes.getStyle().setBorderRadius("4px");


        return attributes;
    }

    public static VerticalLayout createPlayerButtons(QuartettSession quartettSession, Player currentPlayer, Player opponentPlayer, Card card) {
        VerticalLayout attributesLayout = new VerticalLayout();
        attributesLayout.setWidthFull();
        attributesLayout.setPadding(false);
        attributesLayout.setSpacing(false);
        for (String attribute : card.getAttributes().keySet()) {
            Button attributeButton = CreateButton(card, attribute);
            attributesLayout.add(attributeButton);

            attributeButton.addClickListener(event -> {
                attributesLayout.getChildren().forEach(component -> {
                    if (component instanceof Button) {((Button) component).setEnabled(false);}
                });
                if (quartettSession.AlreadyClicked) {
                    return;
                }
                boolean playerWon = quartettSession.onButtonClick(attribute, currentPlayer, opponentPlayer);
                if (playerWon) {
                    attributeButton.getStyle().set("background-color", "green");
                } else {
                    attributeButton.getStyle().set("background-color", "red");
                }
            });

        }
        return attributesLayout;
    }

    public static Button CreateButton(Card card, String attribute) {
        HorizontalLayout buttonContent = new HorizontalLayout();
        buttonContent.setWidthFull();
        buttonContent.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        buttonContent.setAlignItems(FlexComponent.Alignment.CENTER);

        Span attributeName = new Span(attribute + ": ");
        attributeName.getStyle().set("color", "white");
        attributeName.getStyle().set("text-align", "left");
        attributeName.setWidth("50%");

        Span attributeValue = new Span(card.getAttributes().get(attribute).toString());
        attributeValue.getStyle().set("color", "white");
        attributeValue.getStyle().set("text-align", "right");
        attributeValue.setWidth("50%");

        Button attributeButton = new Button(buttonContent);
        attributeButton.setWidthFull();
        attributeButton.getStyle().set("color", "white");
        attributeButton.getStyle().set("background-color", "hsla(214, 10%, 0%, 0.1)");
        attributeButton.getStyle().set("backdrop-filter", "blur(20px)");
        attributeButton.getStyle().setBorder("0.5px solid rgba(40, 44, 26, 0.3)");
        attributeButton.getStyle().setBoxShadow("0 4px 6px rgba(0, 0, 0, 0.1)");
        attributeButton.getStyle().setTransition("all 0.3s ease");
        attributeButton.getStyle().setHeight("30px");
        attributeButton.setDisableOnClick(true);


        buttonContent.add(attributeName, attributeValue);
        buttonContent.getStyle().setMinWidth("290px");
        return attributeButton;
    }

    public static Span CreateCardName(Card card) {
        Span nameLabel = new Span(card.getName());
        nameLabel.getStyle().set("font-weight", "bold")
                .set("display", "block")
                .set("text-align", "left")
                .setPaddingLeft("8px")
                .set("color", "hsla(214, 87%, 92%, 0.8)")
                .setBackground("linear-gradient(to left, rgba(255,0,0,0), hsla(214, 90%, 55%, 0.13))")
                .set("padding", "2px 8px")
                .set("border-radius", "4px");
        return nameLabel;
    }

    public static Div CreateQuartettCardImage(Card card) {
        Div CardImage = new Div();
        Image image = new Image(card.getImagePath(), "Card Image");
        image.getStyle().set("display", "block");
        image.getStyle().set("margin-left", "auto");
        image.getStyle().set("margin-right", "auto").setWidth("300px").setHeight("200px").set("object-fit", "cover");
        image.getStyle().setBorderRadius("4px");
        image.getStyle().setMarginTop("4px");
        image.getStyle().setMarginBottom("2px");
        CardImage.add(image);
        return CardImage;
    }

    public static HorizontalLayout CreateNameAndRole(Player currentPlayer, String role) {
        HorizontalLayout NameAndRole = new HorizontalLayout();
        // Player name
        Span nameLabel = new Span(currentPlayer.getName());
        nameLabel.getStyle().set("font-weight", "bold")
                .set("display", "block")
                .set("text-align", "left")
                .setPaddingLeft("8px")
                .set("color", "hsla(214, 87%, 92%, 0.8)")
                .setBackground("linear-gradient(to left, rgba(255,0,0,0), hsla(214, 90%, 55%, 0.13))")
                .set("padding", "2px 8px")
                .set("border-radius", "4px");
        NameAndRole.add(nameLabel);

        Span PlayerRole = new Span(role);
        PlayerRole.getStyle().set("font-weight", "bold")
                .set("display", "block")
                .set("text-align", "left")
                .setPaddingLeft("8px")
                .set("color", "hsla(214, 87%, 92%, 0.8)")
                .setBackground("linear-gradient(to left, rgba(255,0,0,0), hsla(214, 90%, 55%, 0.13))")
                .set("padding", "2px 8px")
                .set("border-radius", "4px");
        NameAndRole.add(PlayerRole);

        return NameAndRole;
    }

}
