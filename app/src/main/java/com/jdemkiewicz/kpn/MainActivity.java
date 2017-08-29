package com.jdemkiewicz.kpn;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindDrawable(R.drawable.ic_brightness_1_black_24dp)
    Drawable stoneDrawable;

    @BindDrawable(R.drawable.ic_content_cut_black_24dp)
    Drawable cutDrawable;

    @BindDrawable(R.drawable.ic_content_copy_black_24dp)
    Drawable paperDrawable;

    @BindView(R.id.main_user_choice)
    ImageView userChoiceImageView;

    @BindView(R.id.main_komputer_choice)
    ImageView computerChoiceImageView;

    @BindView(R.id.result_text)
    TextView resultText;

    private Random random;
    private ActionEnum myActionEnum;
    private ActionEnum computerActionEnum;
    private int userScore;
    private int computerScore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        random = new Random();
    }

    @OnClick(R.id.main_paper_choice)
    public void setUserChoiceToPapper() {
        setUserChoice(paperDrawable, ActionEnum.PAPER);
    }

    @OnClick(R.id.main_stone_choice)
    public void setUserChoiceToStone() {
        setUserChoice(stoneDrawable, ActionEnum.STONE);
    }

    @OnClick(R.id.main_scissors_choice)
    public void setUserChoiceToScissors() {
        setUserChoice(cutDrawable, ActionEnum.CUT);
    }

    private void setUserChoice(Drawable drawable, ActionEnum actionEnum) {
        userChoiceImageView.setImageDrawable(drawable);
        myActionEnum = actionEnum;
        startGame();
    }

    private void checkScore() {
        if (userScore == 10)
            showAlertDialog("I win!");
        else if (computerScore == 10)
            showAlertDialog("I loose! :(");
    }


    private void startGame() {
        computerActionEnum = getComputerChoice();
        setComputerChoice(computerActionEnum);
        GameResult gameResult = getGameResult();
        addScore(gameResult);
        setResult();
        checkScore();
    }

    private void showAlertDialog(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("End of game")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userScore = 0;
                        computerScore = 0;
                        refreshResult();
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).create();

        alertDialog.show();
    }

    private void setResult() {
        resultText.setText(userScore + " : " + computerScore);
    }

    private void refreshResult() {
        resultText.setText("0 : 0");
    }

    private ActionEnum getComputerChoice() {
        int randomComputerChoice = random.nextInt(3);
        if (randomComputerChoice == 0) {
            return ActionEnum.CUT;
        }
        if (randomComputerChoice == 1) {
            return ActionEnum.STONE;
        }
        return ActionEnum.PAPER;
    }

    private void setComputerChoice(ActionEnum actionEnum) {
        switch (actionEnum) {
            case CUT:
                setComputerImage(cutDrawable);
                break;
            case PAPER:
                setComputerImage(paperDrawable);
                break;
            case STONE:
                setComputerImage(stoneDrawable);
                break;
            default:
        }
    }

    private void setComputerImage(Drawable drawable) {
        computerChoiceImageView.setImageDrawable(drawable);
    }

    private boolean isMyWin() {
        return (myActionEnum.equals(ActionEnum.CUT) && computerActionEnum.equals(ActionEnum.PAPER)) ||
                (myActionEnum.equals(ActionEnum.PAPER) && computerActionEnum.equals(ActionEnum.STONE)) ||
                (myActionEnum.equals(ActionEnum.STONE) && computerActionEnum.equals(ActionEnum.CUT));
    }

    private GameResult getGameResult() {
        if (myActionEnum.equals(computerActionEnum))
            return GameResult.TIE;
        if (isMyWin())
            return GameResult.WIN;
        return GameResult.LOSE;
    }

    private void addScore(GameResult gameResult) {
        if (gameResult.equals(GameResult.WIN))
            userScore++;
        if (gameResult.equals(GameResult.LOSE))
            computerScore++;
    }

    enum ActionEnum {
        CUT,
        STONE,
        PAPER
    }

    enum GameResult {
        WIN,
        LOSE,
        TIE
    }


}
