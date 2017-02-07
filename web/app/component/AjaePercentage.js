import React, {Component} from 'react';

const styles = {
    container: {
        padding: "20px",
        textAlign: "center",
        height: "160px",
        fontSize: "70px",
        fontFamily: "baemin",
        color: "#ffffff",
    }
};

class AjaePercentage extends Component {
    constructor(props) {
        super(props);

        let percentage = props.percentage;

        if(percentage >= 0 && percentage < 70) {
            this.ajaeMessageColor = "not_ajae_color";
        } else if(percentage >= 70 && percentage < 90) {
            this.ajaeMessageColor = "medium_ajae_color";
        } else {
            this.ajaeMessageColor = "full_ajae_color";
        }

        this.nickName = decodeURI(props.nickName);
    }

    render() {
        return (
            <div style={styles.container}>
                <div>{this.nickName} 님의 아재력</div>
                <div style={{marginTop: "10px"}} className={this.ajaeMessageColor}>{this.props.percentage} %</div>
            </div>
        );
    }
}

export default AjaePercentage;