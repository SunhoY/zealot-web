import React, {Component} from 'react';
import AjaeIndicatingImage from 'component/AjaeIndicatingImage';
import AjaeIndicatingText from 'component/AjaeIndicatingText';
import AjaePercentage from 'component/AjaePercentage';

const styles = {
    container: {
        width: "100%",
        height: "100%"
    },
    title: {
        width: "100%",
        paddingTop: "100px",
        textAlign: "center"
    },
    centerAlign: {
        width: "100%",
        display: "block",
        textAlign: "center",
    },
    baeminFont: {
        fontFamily: "baemin"
    },
    textLarge: {
        fontSize: "180px",
        color: "#ffffff"
    },
};

class Landing extends Component {
    render() {
        return (
            <div style={styles.container} className="background">
                <div style={styles.title}>
                    <div style={Object.assign(styles.baeminFont, styles.textLarge)}>아재력</div>
                    <div style={Object.assign(styles.baeminFont, styles.textLarge)}>테스트</div>
                </div>
                <AjaePercentage percentage={this.props.percentage} nickName={this.props.nickName} />
                <AjaeIndicatingImage percentage={this.props.percentage}/>
                <AjaeIndicatingText percentage={this.props.percentage} />
            </div>
        );
    }
}

export default Landing;